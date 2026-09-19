package dev.jjblock21.bst.mixin;

import dev.jjblock21.bst.BstConfig;
import dev.jjblock21.bst.BstMain;
import dev.jjblock21.bst.SmithingPreview;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.CyclingSlotBackground;
import net.minecraft.client.gui.screens.inventory.ItemCombinerScreen;
import net.minecraft.client.gui.screens.inventory.SmithingScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.SmithingMenu;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import org.joml.Quaternionf;
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
    private ArmorStand armorStandPreview;

    // dummy constructor
    public MixinSmithingScreen(SmithingMenu itemCombinerMenu, Inventory inventory, Component component,
                               ResourceLocation resourceLocation) {
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
            target = "Lnet/minecraft/client/gui/screens/inventory/ItemCombinerScreen;<init>(Lnet/minecraft/world/inventory/ItemCombinerMenu;Lnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/network/chat/Component;Lnet/minecraft/resources/ResourceLocation;)V"
        ),
        index = 3
    )
    private static ResourceLocation getSmithingLocation(ResourceLocation old) {
        return new ResourceLocation(BstMain.MOD_ID, "menu.png");
    }

    // prevent recipe error indicator from being drawn over the non-existent arrow
    @Inject(method = "renderErrorIcon", at = @At("HEAD"), cancellable = true)
    private void renderErrorIcon(GuiGraphics guiGraphics, int i, int j, CallbackInfo ci) {
        ci.cancel();
    }

    // hide the weird tooltips that no other menu has
    @Redirect(
        method = "render",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/screens/inventory/SmithingScreen;renderOnboardingTooltips(Lnet/minecraft/client/gui/GuiGraphics;II)V"
        )
    )
    private void renderOnboardingTooltips(SmithingScreen instance, GuiGraphics arg, int i, int j) {
    }

    // hide the weird slot icons
    @Redirect(
        method = "renderBg",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/screens/inventory/CyclingSlotBackground;render(Lnet/minecraft/world/inventory/AbstractContainerMenu;Lnet/minecraft/client/gui/GuiGraphics;FII)V"
        )
    )
    private void renderSlotBackground(CyclingSlotBackground instance, AbstractContainerMenu arg,
                                      GuiGraphics arg2, float f, int i, int j) {
    }

    // customize the display armor stand
    @Inject(method = "subInit", at = @At("TAIL"))
    private void subInit(CallbackInfo ci) {
        SmithingPreview.initArmorStand(armorStandPreview);
    }

    @Inject(method = "updateArmorStandPreview", at = @At("HEAD"))
    private void updateArmorStandPreview(ItemStack item, CallbackInfo ci) {
        if (BstConfig.armlessArmorStand && !(item.getItem() instanceof ArmorItem)) {
            item = ItemStack.EMPTY;
        }
        bst$preview.setDisplayItem(item);
    }

    // if arms are disabled, avoid putting anything into the hands
    @Redirect(
        method = "updateArmorStandPreview",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/decoration/ArmorStand;setItemSlot(Lnet/minecraft/world/entity/EquipmentSlot;Lnet/minecraft/world/item/ItemStack;)V"
        )
    )
    private void setItemSlot(ArmorStand armorStand, EquipmentSlot slot, ItemStack item) {
        if (BstConfig.armlessArmorStand &&
            (slot == EquipmentSlot.MAINHAND || slot == EquipmentSlot.OFFHAND)) {
            return;
        }
        armorStand.setItemSlot(slot, item);
    }

    // render custom movable preview
    @Redirect(
        method = "renderBg",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/screens/inventory/InventoryScreen;renderEntityInInventory(Lnet/minecraft/client/gui/GuiGraphics;IIILorg/joml/Quaternionf;Lorg/joml/Quaternionf;Lnet/minecraft/world/entity/LivingEntity;)V"
        )
    )
    private void renderEntityInInventory(GuiGraphics gfx, int i, int j, int k, Quaternionf q1,
                                         Quaternionf q2, LivingEntity arg2) {
        bst$preview.render(gfx, leftPos, topPos, armorStandPreview);
    }

    @Override
    public boolean mouseClicked(double mx, double my, int button) {
        if (bst$preview.mouseClicked(leftPos, topPos, mx, my, button)) return true;
        return super.mouseClicked(mx, my, button);
    }

    @Override
    public boolean mouseDragged(double mx, double my, int button, double dx, double dy) {
        if (bst$preview.mouseDragged(mx, my, button, dx, dy)) return true;
        return super.mouseDragged(mx, my, button, dx, dy);
    }

    @Override
    public boolean mouseReleased(double mx, double my, int button) {
        if (bst$preview.mouseReleased(mx, my, button)) return true;
        return super.mouseReleased(mx, my, button);
    }
}
