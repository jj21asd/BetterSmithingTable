package me.bettersmithingtable.mixin;

import net.minecraft.world.inventory.SmithingMenu;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SmithingRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(SmithingMenu.class)
public interface SmithingMenuAccessor {

    /**
     * Used to access the list of recipes.
     */
    @Accessor("recipes")
    List<RecipeHolder<SmithingRecipe>> getRecipes();

}
