package me.bettersmithingtable.mixin;

import me.bettersmithingtable.BetterSmithingTable;
import me.bettersmithingtable.ConfigModel;
import net.minecraft.client.gui.DrawContext;
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
import org.joml.Vector3f;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SmithingScreen.class)
public abstract class SmithingScreenMixin extends ForgingScreen<SmithingScreenHandler> {
    @Unique private static final Vector3f STAND_POS;
    @Unique private static final Quaternionf STAND_ROT;

    static {
        STAND_POS = new Vector3f(.025f, 1f, 0f);
        STAND_ROT = new Quaternionf().rotationXYZ(MathHelper.PI * .12f, 0, MathHelper.PI);
    }

    @Unique
    private boolean bst$isPresentingItem;

    @Shadow
    private ArmorStandEntity armorStand;

    public SmithingScreenMixin(SmithingScreenHandler handler, PlayerInventory playerInventory,
                               Text title, Identifier texture) {
        super(handler, playerInventory, title, texture);
    }

    @ModifyArg(
        method = "<init>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/screen/ingame/ForgingScreen;<init>(Lnet/minecraft/screen/ForgingScreenHandler;Lnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/text/Text;Lnet/minecraft/util/Identifier;)V"
        ),
        index = 3
    )
    private static Identifier getTexture(Identifier texture) {
        return BetterSmithingTable.getMenuTexture();
    }

    // leave menu title in the default position
    @Redirect(
        method = "<init>",
        at = @At(
            value = "FIELD",
            opcode = Opcodes.PUTFIELD,
            target = "Lnet/minecraft/client/gui/screen/ingame/SmithingScreen;titleX:I"
        )
    )
    private void assignTitleX(SmithingScreen instance, int value) { }

    @Redirect(
        method = "<init>",
        at = @At(
            value = "FIELD",
            opcode = Opcodes.PUTFIELD,
            target = "Lnet/minecraft/client/gui/screen/ingame/SmithingScreen;titleY:I"
        )
    )
    private void assignTitleY(SmithingScreen instance, int value) { }

    // hide invalid recipe arrow
    @Inject(method = "hasInvalidRecipe", at = @At("HEAD"), cancellable = true)
    private void hasInvalidRecipe(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }

    @Inject(method = "equipArmorStand", at = @At("HEAD"))
    private void equipArmorStand(ItemStack stack, CallbackInfo ci) {
        if (armorStand != null) {
            bst$isPresentingItem = !stack.isEmpty();
        }
    }

    @Inject(
        method = "drawBackground",
        at = @At(
            value = "INVOKE",
            shift = At.Shift.AFTER,
            target = "Lnet/minecraft/client/gui/screen/ingame/ForgingScreen;drawBackground(Lnet/minecraft/client/gui/DrawContext;FII)V"
        ),
        cancellable = true
    )
    private void drawBackground(DrawContext context, float framesPerTick, int mouseX, int mouseY, CallbackInfo ci) {
        if (bst$isPresentingItem) {
            // convert to degrees per second
            armorStand.bodyYaw -= ConfigModel.rotationSpeed * framesPerTick * .5f;
        } else {
            armorStand.bodyYaw = 200;
        }

        InventoryScreen.drawEntity(context, x + 93, y + 15, x + 129, y + 71, 25,
            STAND_POS, STAND_ROT, new Quaternionf(), armorStand);

        ci.cancel();
    }

    // hide slot tooltips
    @Redirect(
        method = "render",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/screen/ingame/SmithingScreen;renderSlotTooltip(Lnet/minecraft/client/gui/DrawContext;II)V"
        )
    )
    private void renderSlotTooltip(SmithingScreen instance, DrawContext context, int mouseX, int mouseY) { }
}