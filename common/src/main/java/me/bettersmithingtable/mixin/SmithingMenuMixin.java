package me.bettersmithingtable.mixin;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.crafting.RecipeHolder;
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
    private List<RecipeHolder<SmithingRecipe>> recipes;

    public SmithingMenuMixin(MenuType<?> menuType, int i, Inventory inventory, ContainerLevelAccess containerLevelAccess) {
        super(menuType, i, inventory, containerLevelAccess);
    }

    /*
     * Move the slots to fit the new gui.
     */
    @Inject(method = "createInputSlotDefinitions", at = @At("HEAD"), cancellable = true)
    public void createInputSlotDefinitions(CallbackInfoReturnable<ItemCombinerMenuSlotDefinition> cir) {
        ItemCombinerMenuSlotDefinition definition = ItemCombinerMenuSlotDefinition.create()
                .withSlot(0, 64, 35, (stack -> recipes.stream().anyMatch((holder) ->
                        holder.value().isTemplateIngredient(stack)
                )))
                // Armor piece
                .withSlot(1, 38, 45, (stack -> recipes.stream().anyMatch((holder) ->
                        holder.value().isBaseIngredient(stack)
                )))
                // Trim/Upgrade material
                .withSlot(2, 18, 25, (stack -> recipes.stream().anyMatch((holder) ->
                        holder.value().isAdditionIngredient(stack)
                )))
                .withResultSlot(3, 142, 35).build();

        cir.setReturnValue(definition);
    }
}
