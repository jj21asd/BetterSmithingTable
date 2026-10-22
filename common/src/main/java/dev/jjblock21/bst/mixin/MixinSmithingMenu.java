package dev.jjblock21.bst.mixin;

import net.minecraft.world.inventory.ItemCombinerMenuSlotDefinition;
import net.minecraft.world.inventory.SmithingMenu;
import net.minecraft.world.item.crafting.RecipeAccess;
import net.minecraft.world.item.crafting.RecipePropertySet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SmithingMenu.class)
public class MixinSmithingMenu {
    // reposition the slots to fit the custom menu texture
    @Inject(method = "createInputSlotDefinitions", at = @At("HEAD"), cancellable = true)
    private static void createInputSlotDefinitions(final RecipeAccess recipes,
                                                   CallbackInfoReturnable<ItemCombinerMenuSlotDefinition> cir) {
        RecipePropertySet template = recipes.propertySet(RecipePropertySet.SMITHING_TEMPLATE);
        RecipePropertySet substrate = recipes.propertySet(RecipePropertySet.SMITHING_BASE);
        RecipePropertySet material = recipes.propertySet(RecipePropertySet.SMITHING_ADDITION);

        ItemCombinerMenuSlotDefinition defs = ItemCombinerMenuSlotDefinition.create()
            .withSlot(0, 64, 35, template::test)
            .withSlot(1, 38, 45, substrate::test)
            .withSlot(2, 18, 25, material::test)
            .withResultSlot(3, 142, 35)
            .build();

        cir.setReturnValue(defs);
    }
}
