package me.bettersmithingtable.mixin;

import me.bettersmithingtable.BetterSmithingTable;
import me.bettersmithingtable.SmithingPreviewRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.CyclingSlotIcon;
import net.minecraft.client.gui.screen.ingame.ForgingScreen;
import net.minecraft.client.gui.screen.ingame.SmithingScreen;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.SmithingScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
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
    @Unique private SmithingPreviewRenderer bst$previewRenderer;
    @Shadow private ArmorStandEntity armorStand;

    // Dummy constructor
    public SmithingScreenMixin(SmithingScreenHandler handler, PlayerInventory playerInventory,
                               Text title, Identifier texture) {
        super(handler, playerInventory, title, texture);
    }

    @Inject(method = "<init>", at = @At(value = "TAIL"))
    private void setup(CallbackInfo ci) {
        bst$previewRenderer = new SmithingPreviewRenderer();
    }

    // Replace menu texture
    @ModifyArg(
        method = "<init>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/screen/ingame/ForgingScreen;<init>(Lnet/minecraft/screen/ForgingScreenHandler;Lnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/text/Text;Lnet/minecraft/util/Identifier;)V"
        ),
        index = 3
    )
    private static Identifier getTexture(Identifier old) {
        return BetterSmithingTable.getMenuTexture();
    }

    // Don't move title text around
    @Redirect(
        method = "<init>",
        at = @At(
            value = "FIELD",
            opcode = Opcodes.PUTFIELD,
            target = "Lnet/minecraft/client/gui/screen/ingame/SmithingScreen;titleX:I"
        )
    )
    private void assignTitleX(SmithingScreen instance, int value) {}

    @Redirect(
        method = "<init>",
        at = @At(
            value = "FIELD",
            opcode = Opcodes.PUTFIELD,
            target = "Lnet/minecraft/client/gui/screen/ingame/SmithingScreen;titleY:I"
        )
    )
    private void assignTitleY(SmithingScreen instance, int value) {}

    // Hide invalid recipe arrow
    @Inject(method = "hasInvalidRecipe", at = @At("HEAD"), cancellable = true)
    private void hasInvalidRecipe(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }

    @Inject(method = "equipArmorStand", at = @At("HEAD"))
    private void equipArmorStand(ItemStack stack, CallbackInfo ci) {
        if (armorStand != null) {
            bst$previewRenderer.setPresentingItem(!stack.isEmpty());
        }
    }

    // Hide dynamic slot icons
    @Redirect(
        method = "drawBackground",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/screen/ingame/CyclingSlotIcon;render(Lnet/minecraft/screen/ScreenHandler;Lnet/minecraft/client/gui/DrawContext;FII)V")
    )
    private void renderSlotIcons(CyclingSlotIcon instance, ScreenHandler screenHandler, DrawContext context, float delta, int x, int y) {}

    // Hide slot tooltips
    @Redirect(
        method = "render",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/screen/ingame/SmithingScreen;renderSlotTooltip(Lnet/minecraft/client/gui/DrawContext;II)V"
        )
    )
    private void renderSlotTooltip(SmithingScreen instance, DrawContext context, int mouseX, int mouseY) {}

    // Render the display armor stand differently
    @Redirect(
        method = "drawBackground",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/screen/ingame/InventoryScreen;drawEntity(Lnet/minecraft/client/gui/DrawContext;IIILorg/joml/Quaternionf;Lorg/joml/Quaternionf;Lnet/minecraft/entity/LivingEntity;)V"
        )
    )
    private void drawBackground(DrawContext context, int posX, int posY, int size, Quaternionf q1, Quaternionf q2, LivingEntity entity) {
        bst$previewRenderer.draw(context, x, y, armorStand);
    }

    // Intercept mouse events
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (bst$previewRenderer.handleClick(x, y, mouseX, mouseY, button)) {
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (bst$previewRenderer.handleDrag(deltaX)) {
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (bst$previewRenderer.handleRelease(button)) {
            return true;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }
}
