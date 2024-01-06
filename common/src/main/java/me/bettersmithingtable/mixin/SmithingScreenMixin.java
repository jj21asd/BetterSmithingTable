package me.bettersmithingtable.mixin;

import me.bettersmithingtable.BetterSmithingTable;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.CyclingSlotBackground;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.inventory.ItemCombinerScreen;
import net.minecraft.client.gui.screens.inventory.SmithingScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.SmithingMenu;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SmithingScreen.class)
public abstract class SmithingScreenMixin extends ItemCombinerScreen<SmithingMenu> {

    @Shadow
    private ArmorStand armorStandPreview;

    // Dummy constructor to satisfy java
    public SmithingScreenMixin(SmithingMenu itemCombinerMenu, Inventory inventory, Component component, ResourceLocation resourceLocation) {
        super(itemCombinerMenu, inventory, component, resourceLocation);
    }

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
     * Define custom code for rendering the background to remove the CyclingSlotIcons and customize how the armor stand is drawn.
     */
    @Inject(method = "renderBg", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/gui/screens/inventory/ItemCombinerScreen;renderBg(Lnet/minecraft/client/gui/GuiGraphics;FII)V"), cancellable = true)
    private void renderBg(GuiGraphics guiGraphics, float f, int i, int j, CallbackInfo ci) {

        InventoryScreen.renderEntityInInventory(guiGraphics, leftPos + 111, topPos + 67, 25, new Vector3f(),
                BetterSmithingTable.ARMOR_STAND_ROTATION, null, armorStandPreview);

        ci.cancel(); // Don't execute the rest of the function.
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
    private void renderSlotTooltip(SmithingScreen instance, GuiGraphics guiGraphics, int i, int j) {
        // Currently this does nothing to hide all tooltips, but this can be changed in the future.
    }
}
