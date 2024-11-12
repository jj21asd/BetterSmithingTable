package me.smithingui.mixin;

import me.smithingui.SmithingUI;
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
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SmithingScreen.class)
public abstract class SmithingScreenMixin extends ItemCombinerScreen<SmithingMenu> {
    @Unique
    private static final ResourceLocation MENU_IMG =
            new ResourceLocation(SmithingUI.MOD_ID, "menu.png");
    @Unique
    private static final Quaternionf STAND_ROT = (new Quaternionf())
            .rotationXYZ((float)Math.PI * 0.12f, 0, (float)Math.PI);

    @Shadow
    private ArmorStand armorStandPreview;

    // Dummy constructor
    public SmithingScreenMixin(SmithingMenu itemCombinerMenu, Inventory inventory,
                               Component component, ResourceLocation resourceLocation) {
        super(itemCombinerMenu, inventory, component, resourceLocation);
    }

    // Prevent title position from being set
    @Redirect(method = "<init>", at = @At(value = "FIELD", opcode = Opcodes.PUTFIELD,
            target = "Lnet/minecraft/client/gui/screens/inventory/SmithingScreen;titleLabelX:I"))
    private void assignTitleX(SmithingScreen instance, int value) { }

    @Redirect(method = "<init>", at = @At(value = "FIELD", opcode = Opcodes.PUTFIELD,
            target = "Lnet/minecraft/client/gui/screens/inventory/SmithingScreen;titleLabelY:I"))
    private void assignTitleY(SmithingScreen instance, int value) { }

    @Inject(method = "hasRecipeError", at = @At("HEAD"), cancellable = true)
    private void hasRecipeError(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false); // Hide the invalid recipe arrow
    }

    @Inject(method = "renderBg", at = @At(value = "INVOKE", shift = At.Shift.AFTER,
            target = "Lnet/minecraft/client/gui/screens/inventory/ItemCombinerScreen;renderBg(Lnet/minecraft/client/gui/GuiGraphics;FII)V"), cancellable = true)
    private void renderBg(GuiGraphics guiGraphics, float f, int i, int j, CallbackInfo ci) {
        InventoryScreen.renderEntityInInventory(guiGraphics, leftPos + 111, topPos + 67, 25,
                STAND_ROT, new Quaternionf(), armorStandPreview);
        ci.cancel(); // Skip the rest of the function
    }

    @ModifyArg(method = "<init>", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/gui/screens/inventory/ItemCombinerScreen;<init>(Lnet/minecraft/world/inventory/ItemCombinerMenu;Lnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/network/chat/Component;Lnet/minecraft/resources/ResourceLocation;)V"),
            index = 3)
    private static ResourceLocation getTextureInSuperConstructor(ResourceLocation resourceLocation) {
        return MENU_IMG;
    }

    @Redirect(method = "subInit", at = @At(value = "FIELD", opcode = Opcodes.PUTFIELD,
            target = "Lnet/minecraft/world/entity/decoration/ArmorStand;yBodyRot:F"))
    private void assignBodyYaw(ArmorStand instance, float value) {
        instance.yBodyRot = 200; // Custom armor stand yaw
    }

    @Redirect(method = "render", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/gui/screens/inventory/SmithingScreen;renderOnboardingTooltips(Lnet/minecraft/client/gui/GuiGraphics;II)V"))
    private void renderSlotTooltip(SmithingScreen instance, GuiGraphics guiGraphics, int i, int j) {
        // Hide all tooltips
    }
}
