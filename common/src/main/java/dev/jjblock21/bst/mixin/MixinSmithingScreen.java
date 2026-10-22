package dev.jjblock21.bst.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.jjblock21.bst.BstConfig;
import dev.jjblock21.bst.BstMain;
import dev.jjblock21.bst.core.SmithingPreview;
import dev.jjblock21.bst.core.SmithingPreviewHolder;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.CyclingSlotBackground;
import net.minecraft.client.gui.screens.inventory.ItemCombinerScreen;
import net.minecraft.client.gui.screens.inventory.SmithingScreen;
import net.minecraft.client.renderer.entity.state.ArmorStandRenderState;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.SmithingMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.Equippable;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SmithingScreen.class)
public abstract class MixinSmithingScreen extends ItemCombinerScreen<SmithingMenu> implements SmithingPreviewHolder {
    @Shadow
    @Final
    private ArmorStandRenderState armorStandPreview;

    @Unique
    private SmithingPreview bst$preview;

    // dummy constructor
    public MixinSmithingScreen(SmithingMenu itemCombinerMenu, Inventory inventory, Component component,
                               Identifier identifier) {
        super(itemCombinerMenu, inventory, component, identifier);
    }

    @Override
    public SmithingPreview bst$getSmithingPreview() {
        return bst$preview;
    }

    // replace the menu texture
    @ModifyArg(
        method = "<init>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/screens/inventory/ItemCombinerScreen;<init>(Lnet/minecraft/world/inventory/ItemCombinerMenu;Lnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/network/chat/Component;Lnet/minecraft/resources/Identifier;)V"
        ),
        index = 3
    )
    private static Identifier getSmithingLocation(Identifier old) {
        return Identifier.fromNamespaceAndPath(BstMain.MOD_ID, "menu.png");
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void constructor(SmithingMenu menu, Inventory inv, Component component, CallbackInfo ci) {
        bst$preview = new SmithingPreview();

        // customize title text position
        titleLabelX = 8;
        titleLabelY = 6;
    }

    // customize the display armor stand
    @Inject(method = "subInit", at = @At("TAIL"))
    private void subInit(CallbackInfo ci) {
        SmithingPreview.initArmorStand(armorStandPreview);
    }

    // prevent recipe error indicator from being drawn over the non-existent arrow
    @Inject(method = "renderErrorIcon", at = @At("HEAD"), cancellable = true)
    private void renderErrorIcon(GuiGraphics guiGraphics, int i, int j, CallbackInfo ci) {
        ci.cancel();
    }

    // hide the weird tooltips that no other menu has
    @Inject(method = "renderOnboardingTooltips", at = @At("HEAD"), cancellable = true)
    private void renderOnboardingTooltips(GuiGraphics gfx, int i, int j, CallbackInfo ci) {
        ci.cancel();
    }

    // hide the weird slot icons
    @WrapWithCondition(
        method = "renderBg",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/screens/inventory/CyclingSlotBackground;render(Lnet/minecraft/world/inventory/AbstractContainerMenu;Lnet/minecraft/client/gui/GuiGraphics;FII)V"
        )
    )
    private boolean shouldRenderSlotBackground(CyclingSlotBackground instance, AbstractContainerMenu arg,
                                               GuiGraphics arg2, float f, int i, int j) {
        return false;
    }

    // if arms are disabled, avoid putting anything into the hands
    @Inject(method = "updateArmorStandPreview", at = @At("TAIL"))
    private void updateArmorStandPreview(ItemStack item, CallbackInfo ci) {
        if (BstConfig.armlessArmorStand) {
            Equippable equippable = item.get(DataComponents.EQUIPPABLE);
            if (!item.isEmpty() && equippable == null) {
                item = ItemStack.EMPTY;

                // clear item assigned by vanilla code
                armorStandPreview.leftHandItemStack = ItemStack.EMPTY;
                armorStandPreview.leftHandItemState.clear();
            }
        }
        bst$preview.setDisplayItem(item);
    }

    // render custom movable preview
    @WrapOperation(
        method = "renderBg",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/GuiGraphics;submitEntityRenderState(Lnet/minecraft/client/renderer/entity/state/EntityRenderState;FLorg/joml/Vector3f;Lorg/joml/Quaternionf;Lorg/joml/Quaternionf;IIII)V"
        )
    )
    private void renderEntityInInventory(GuiGraphics graphics, EntityRenderState entityRenderState, float f,
                                         Vector3f pos, Quaternionf rot, Quaternionf camAngle, int i, int j,
                                         int k, int l, Operation<Void> original) {
        bst$preview.render(graphics, leftPos, topPos, armorStandPreview);
    }
}
