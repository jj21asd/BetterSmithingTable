package me.bettersmithingtable.mixin;

import me.bettersmithingtable.BetterSmithingTable;
import me.bettersmithingtable.Config;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screen.ingame.ForgingScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.screen.ingame.SmithingScreen;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.SmithingScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.joml.Quaternionf;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SmithingScreen.class)
public abstract class SmithingScreenMixin extends ForgingScreen<SmithingScreenHandler> {
    @Unique
    private boolean bst$isPresentingItem;

    @Unique
    private static final Quaternionf STAND_ROT = new Quaternionf()
            .rotationXYZ(MathHelper.PI * 0.12f, 0, MathHelper.PI);

    @Shadow
    private ArmorStandEntity display;

    public SmithingScreenMixin(SmithingScreenHandler handler, PlayerInventory playerInventory,
                               Text title, Identifier texture) {
        super(handler, playerInventory, title, texture);
    }

    @ModifyArg(method = "<init>", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/gui/screen/ingame/ForgingScreen;<init>(Lnet/minecraft/screen/ForgingScreenHandler;Lnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/text/Text;Lnet/minecraft/util/Identifier;)V"),
            index = 3)
    private static Identifier getTexture(Identifier identifier) {
        return BetterSmithingTable.getMenuTexture();
    }

    /*
     * Leave title text at default position
     */
    @Redirect(method = "<init>", at = @At(value = "FIELD", opcode = Opcodes.PUTFIELD,
              target = "Lnet/minecraft/client/gui/screen/ingame/SmithingScreen;titleX:I"))
    private void assignTitleX(SmithingScreen instance, int value) { }

    @Redirect(method = "<init>", at = @At(value = "FIELD", opcode = Opcodes.PUTFIELD,
              target = "Lnet/minecraft/client/gui/screen/ingame/SmithingScreen;titleY:I"))
    private void assignTitleY(SmithingScreen instance, int value) { }

    /*
     * Hide invalid recipe arrow
     */
    @Inject(method = "isRecipeError", at = @At("HEAD"), cancellable = true)
    private void hasRecipeError(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }

    /*
     * Prevent dynamic slot icons from being drawn
     */
    @Inject(method = "drawBackground", at = @At(value = "INVOKE", shift = At.Shift.AFTER,
            target = "Lnet/minecraft/client/gui/screen/ingame/ForgingScreen;drawBackground(Lnet/minecraft/client/gui/GuiGraphics;FII)V"), cancellable = true)
    private void renderBg(GuiGraphics guiGraphics, float f, int i, int j, CallbackInfo ci) {
        InventoryScreen.drawEntity(guiGraphics, x + 111, y + 67, 25, STAND_ROT, new Quaternionf(), display);
        ci.cancel();
    }

    /*
     * Hide slot tooltips
     */
    @Redirect(method = "render", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/gui/screen/ingame/SmithingScreen;renderTooltips(Lnet/minecraft/client/gui/GuiGraphics;II)V"))
    private void renderSlotTooltip(SmithingScreen instance, GuiGraphics guiGraphics, int i, int j) { }

    /*
     * Rotate armor stand while displaying item
     */
    @Inject(method = "displayStack", at = @At("HEAD"))
    private void displayStack(ItemStack stack, CallbackInfo ci) {
        if (display != null) {
            bst$isPresentingItem = !stack.isEmpty();
        }
    }

    @Inject(method = "drawBackground", at = @At("HEAD"))
    private void drawBackground(GuiGraphics graphics, float framesPerTick, int mouseX, int mouseY, CallbackInfo ci) {
        if (bst$isPresentingItem) {
            // convert to degrees per second
            display.bodyYaw -= Config.rotationSpeed / 2.0f * framesPerTick;
        } else {
            display.bodyYaw = 200;
        }
    }
}
