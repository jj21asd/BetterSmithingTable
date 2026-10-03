package dev.jjblock21.bst.mixin;

import dev.jjblock21.bst.BstConfig;
import dev.jjblock21.bst.BstMain;
import dev.jjblock21.bst.SmithingPreview;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.CyclingSlotBackground;
import net.minecraft.client.gui.screens.inventory.ItemCombinerScreen;
import net.minecraft.client.gui.screens.inventory.SmithingScreen;
import net.minecraft.client.input.MouseButtonEvent;
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
import org.jspecify.annotations.NonNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SmithingScreen.class)
public abstract class MixinSmithingScreen extends ItemCombinerScreen<SmithingMenu> {
    @Unique
    private SmithingPreview bst$preview;

    @Shadow
    @Final
    private ArmorStandRenderState armorStandPreview;

    // dummy constructor
    public MixinSmithingScreen(SmithingMenu itemCombinerMenu, Inventory inventory, Component component,
                               Identifier resourceLocation) {
        super(itemCombinerMenu, inventory, component, resourceLocation);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void constructor(SmithingMenu menu, Inventory inv, Component component, CallbackInfo ci) {
        bst$preview = new SmithingPreview();

        // customize title text position
        titleLabelX = 8;
        titleLabelY = 6;
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
    private static Identifier getTextureLocation(Identifier old) {
        return Identifier.fromNamespaceAndPath(BstMain.MOD_ID, "menu.png");
    }

    // prevent recipe error indicator from being drawn over the non-existent arrow
    @Inject(method = "extractErrorIcon", at = @At("HEAD"), cancellable = true)
    private void extractErrorIcon(final GuiGraphicsExtractor graphics, int i, int j, CallbackInfo ci) {
        ci.cancel();
    }

    // hide the weird tooltips that no other menu has
    @Redirect(
        method = "extractRenderState",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/screens/inventory/SmithingScreen;extractOnboardingTooltips(Lnet/minecraft/client/gui/GuiGraphicsExtractor;II)V"
        )
    )
    private void renderOnboardingTooltips(SmithingScreen instance, GuiGraphicsExtractor graphics,
                                          int mouseX, int mouseY) {
    }

    // hide the weird slot icons
    @Redirect(
        method = "extractBackground",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/screens/inventory/CyclingSlotBackground;extractRenderState(Lnet/minecraft/world/inventory/AbstractContainerMenu;Lnet/minecraft/client/gui/GuiGraphicsExtractor;FII)V"
        )
    )
    private void renderSlotBackground(CyclingSlotBackground instance, AbstractContainerMenu menu,
                                      GuiGraphicsExtractor graphics, float a, int left, int top) {
    }

    // customize the display armor stand
    @Inject(method = "subInit", at = @At("TAIL"))
    private void subInit(CallbackInfo ci) {
        SmithingPreview.initArmorStand(armorStandPreview);
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
    @Redirect(
        method = "extractBackground",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;entity(Lnet/minecraft/client/renderer/entity/state/EntityRenderState;FLorg/joml/Vector3f;Lorg/joml/Quaternionf;Lorg/joml/Quaternionf;IIII)V"
        )
    )
    private void extractEntity(GuiGraphicsExtractor graphics, EntityRenderState renderState, float scale,
                               Vector3f pos, Quaternionf rot, Quaternionf camAngle, int x0, int y0, int x1, int y1) {
        bst$preview.extract(graphics, leftPos, topPos, armorStandPreview);
    }

    @Override
    public boolean mouseClicked(@NonNull MouseButtonEvent event, boolean doubleClick) {
        if (bst$preview.mouseClicked(event, leftPos, topPos)) return true;
        return super.mouseClicked(event, doubleClick);
    }

    @Override
    public boolean mouseReleased(@NonNull MouseButtonEvent event) {
        if (bst$preview.mouseReleased(event)) return true;
        return super.mouseReleased(event);
    }

    @Override
    public boolean mouseDragged(@NonNull MouseButtonEvent event, double dx, double dy) {
        if (bst$preview.mouseDragged(dx)) return true;
        return super.mouseDragged(event, dx, dy);
    }
}
