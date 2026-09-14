package me.bettersmithingtable.mixin.compat;

import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.SmithingScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

// Fixes issue #13
// I don't know if there's an easier way to do this, a JeiPlugin can't remove click areas sadly
// Pseudo will spam the console a bit when JEI is not installed,
// but I can't be bothered to write a mixin config plugin
@Pseudo
@Mixin(targets = "mezz.jei.library.plugins.vanilla.VanillaPlugin")
public class JeiVanillaPluginMixin {
    @Redirect(
        method = "registerGuiHandlers",
        at = @At(
            value = "INVOKE",
            target = "Lmezz/jei/api/registration/IGuiHandlerRegistration;addRecipeClickArea(Ljava/lang/Class;IIII[Lmezz/jei/api/recipe/RecipeType;)V"
        ),
        remap = false
    )
    private <T extends HandledScreen<?>> void redirectSmithingClickArea(
        IGuiHandlerRegistration instance, Class<? extends T> clazz, int x, int y, int width,
        int height,  RecipeType<?>[] recipeTypes) {
        if (clazz != SmithingScreen.class) {
            // Register normally
            instance.addRecipeClickArea(clazz, x, y, width, height, recipeTypes);
            return;
        }

        // Register custom click areas
        // Use the hammer and fire icons in the UI instead of the arrow in vanilla
        instance.addRecipeClickArea(clazz, 40,27, 12, 12, recipeTypes);
        instance.addRecipeClickArea(clazz, 20,47, 12, 12, recipeTypes);
    }
}