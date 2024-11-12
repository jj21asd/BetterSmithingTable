package me.smithingui.mixin;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.crafting.SmithingRecipe;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(SmithingMenu.class)
public abstract class SmithingMenuMixin extends ItemCombinerMenu {
    @Shadow @Final
    private List<SmithingRecipe> recipes;

    public SmithingMenuMixin(MenuType<?> menuType, int i, Inventory inventory,
                             ContainerLevelAccess containerLevelAccess) {
        super(menuType, i, inventory, containerLevelAccess);
    }

    @Inject(method = "createInputSlotDefinitions", at = @At("HEAD"), cancellable = true)
    public void createInputSlotDefinitions(CallbackInfoReturnable<ItemCombinerMenuSlotDefinition> cir) {
        // Move slots to fit gui
        ItemCombinerMenuSlotDefinition def = ItemCombinerMenuSlotDefinition.create()
                .withSlot(0, 64, 35, (stack) -> {
                    return this.recipes.stream().anyMatch((recipe) -> {
                        return recipe.isTemplateIngredient(stack);
                    });
                }).withSlot(1, 38, 45, (stack) -> { // Armor piece
                    return this.recipes.stream().anyMatch((recipe) -> {
                        return recipe.isBaseIngredient(stack);
                    });
                }).withSlot(2, 18, 25, (stack) -> { // Trim/Upgrade material
                    return this.recipes.stream().anyMatch((recipe) -> {
                        return recipe.isAdditionIngredient(stack);
                    });
                }).withResultSlot(3, 142, 35).build();
        cir.setReturnValue(def);
    }
}
