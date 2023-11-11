package me.bettersmithingtable.mixin;

import me.bettersmithingtable.BetterSmithingTable;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.CyclingSlotBackground;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.inventory.SmithingScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SmithingScreen.class)
public abstract class SmithingScreenMixin {

    /*
     * Prevent changing the title label position to have it show up at the default position.
     */
    @Redirect(method = "<init>", at = @At(value = "FIELD", opcode = Opcodes.PUTFIELD,
            target = "Lnet/minecraft/client/gui/screens/inventory/SmithingScreen;titleLabelX:I"))
    private void assignTitleX(SmithingScreen instance, int value) { }

    @Redirect(method = "<init>", at = @At(value = "FIELD", opcode = Opcodes.PUTFIELD,
            target = "Lnet/minecraft/client/gui/screens/inventory/SmithingScreen;titleLabelY:I"))
    private void assignTitleY(SmithingScreen instance, int value) { }


    @Inject(method = "hasRecipeError", at = @At("HEAD"), cancellable = true)
    private void hasRecipeError(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false); // Always return false to hide the invalid recipe arrow.
    }

    /*
     * Redirect CyclingSlotBackground.render to hide dynamic slot icons.
     */
    @Redirect(method = "renderBg", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/gui/screens/inventory/CyclingSlotBackground;render(Lnet/minecraft/world/inventory/AbstractContainerMenu;Lnet/minecraft/client/gui/GuiGraphics;FII)V"))
    private void renderCyclingSlotIcon(CyclingSlotBackground instance, AbstractContainerMenu abstractContainerMenu, GuiGraphics guiGraphics, float f, int i, int j) { }

    /**
     * Swap the texture passed into the constructor of ItemCombinerScreen with the updated texture.
     */
    @ModifyArg(method = "<init>", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/gui/screens/inventory/ItemCombinerScreen;<init>(Lnet/minecraft/world/inventory/ItemCombinerMenu;Lnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/network/chat/Component;Lnet/minecraft/resources/ResourceLocation;)V"),
            index = 3)

    private static ResourceLocation getTextureInSuperConstructor(ResourceLocation resourceLocation) {
        return BetterSmithingTable.SMITHING_MENU_LOCATION;
    }

    /**
     * Redirect assignment to customize armor stand yaw value.
     */
    @Redirect(method = "subInit", at = @At(value = "FIELD", opcode = Opcodes.PUTFIELD,
            target = "Lnet/minecraft/world/entity/decoration/ArmorStand;yBodyRot:F"))
    private void assignBodyYaw(ArmorStand instance, float value) {
        // Use custom armor stand yaw.
        instance.yBodyRot = BetterSmithingTable.ARMOR_STAND_YAW;
    }

    /**
     * Redirect renderOnboardingTooltips to draw custom tooltips.
     */
    @Redirect(method = "render", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/gui/screens/inventory/SmithingScreen;renderOnboardingTooltips(Lnet/minecraft/client/gui/GuiGraphics;II)V"))
    private void renderSLotTooltip(SmithingScreen instance, GuiGraphics guiGraphics, int i, int j) {
        // Currently this does nothing to hide all tooltips, but this can be changed in the future.
    }

    /**
     * Redirect call to InventoryScreen.drawEntity to draw armor stand with custom rotation and offset.
     */
    @Redirect(method = "renderBg", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/gui/screens/inventory/InventoryScreen;renderEntityInInventory(Lnet/minecraft/client/gui/GuiGraphics;FFILorg/joml/Vector3f;Lorg/joml/Quaternionf;Lorg/joml/Quaternionf;Lnet/minecraft/world/entity/LivingEntity;)V"))

    private void drawArmorStandPreview(GuiGraphics guiGraphics, float x, float y, int size, Vector3f vec, Quaternionf rotation, Quaternionf q2, LivingEntity entity) {
        // Little hack to get back the original x and y
        x -= 141;
        y -= 75;

        // Draw the armor stand at its new position:
        InventoryScreen.renderEntityInInventory(guiGraphics, x + 111, y + 67, size, vec, BetterSmithingTable.ARMOR_STAND_ROTATION, q2, entity);
    }
}
