package jjblock21.bettersmithingtable.mixin;

import net.minecraft.recipe.SmithingRecipe;
import net.minecraft.screen.SmithingScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(SmithingScreenHandler.class)
public interface SmithingScreenHandlerAccessor {

    /**
     * Used to access the list of recipes.
     */
    @Accessor("recipes")
    List<SmithingRecipe> getRecipes();

}
