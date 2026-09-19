package dev.jjblock21.bst.mixin;

import net.minecraft.world.inventory.ItemCombinerMenuSlotDefinition;
import net.minecraft.world.inventory.SmithingMenu;
import net.minecraft.world.item.crafting.SmithingRecipe;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(SmithingMenu.class)
public class MixinSmithingMenu {
    @Shadow
    @Final
    private List<SmithingRecipe> recipes;

    // reposition the slots to fit the custom menu texture
    @Inject(method = "createInputSlotDefinitions", at = @At("HEAD"), cancellable = true)
    private void createInputSlotDefinitions(CallbackInfoReturnable<ItemCombinerMenuSlotDefinition> cir) {
        ItemCombinerMenuSlotDefinition defs = new ItemCombinerMenuSlotDefinition.Builder()
            .withSlot(0, 64, 35, item ->
                recipes.stream().anyMatch(recipe -> recipe.isTemplateIngredient(item)))
            .withSlot(1, 38, 45, item ->
                recipes.stream().anyMatch(recipe -> recipe.isBaseIngredient(item)))
            .withSlot(2, 18, 25, item ->
                recipes.stream().anyMatch(recipe -> recipe.isAdditionIngredient(item)))
            .withResultSlot(3, 142, 35)
            .build();

        cir.setReturnValue(defs);
    }
}
