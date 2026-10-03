package dev.jjblock21.bst.mixin.compat;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.jjblock21.bst.RequiresMod;
import mezz.jei.api.recipe.types.IRecipeType;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import net.minecraft.client.gui.screens.inventory.SmithingScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

// fix https://github.com/jj21asd/BetterSmithingTable/issues/13
@RequiresMod("jei")
@Mixin(targets = "mezz.jei.library.plugins.vanilla.VanillaPlugin")
public class MixinJeiVanillaPlugin {
    @WrapOperation(
        remap = false,
        method = "registerGuiHandlers",
        at = @At(
            value = "INVOKE",
            target = "Lmezz/jei/api/registration/IGuiHandlerRegistration;addRecipeClickArea(Ljava/lang/Class;IIII[Lmezz/jei/api/recipe/types/IRecipeType;)V"
        )
    )
    private void addRecipeClickArea(IGuiHandlerRegistration instance, Class<?> screenClass, int x, int y,
                                    int width, int height, IRecipeType<?>[] recipeTypes, Operation<Void> original) {
        if (screenClass == SmithingScreen.class) {
            // register custom click areas instead
            // use the hammer and fire icons instead of the arrow from vanilla
            original.call(instance, screenClass, 40, 27, 12, 12, recipeTypes);
            original.call(instance, screenClass, 20, 47, 12, 12, recipeTypes);
        } else {
            original.call(instance, screenClass, x, y, width, height, recipeTypes);
        }
    }
}
