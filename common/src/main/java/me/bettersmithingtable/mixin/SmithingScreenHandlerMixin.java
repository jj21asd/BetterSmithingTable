package me.bettersmithingtable.mixin;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.recipe.SmithingRecipe;
import net.minecraft.screen.ForgingScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.SmithingScreenHandler;
import net.minecraft.screen.slot.ForgingSlotsManager;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(SmithingScreenHandler.class)
public abstract class SmithingScreenHandlerMixin extends ForgingScreenHandler {
    @Shadow @Final
    private List<SmithingRecipe> recipes;

    public SmithingScreenHandlerMixin(@Nullable ScreenHandlerType<?> type, int syncId,
                                      PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(type, syncId, playerInventory, context);
    }

    /*
     * Reposition slots to fit new texture
     */
    @Inject(method = "getForgingSlotsManager", at = @At("HEAD"), cancellable = true)
    public void getForgingSlotsManager(CallbackInfoReturnable<ForgingSlotsManager> cir) {
        ForgingSlotsManager man = ForgingSlotsManager.create()
                .input(0, 64, 35, stack -> {
                    return this.recipes.stream().anyMatch(recipe -> {
                        return recipe.testTemplate(stack); // smithing template
                    });
                }).input(1, 38, 45, stack -> {
                    return this.recipes.stream().anyMatch(recipe -> {
                        return recipe.testBase(stack); // armor piece
                    });
                }).input(2, 18, 25, stack -> {
                    return this.recipes.stream().anyMatch(recipe -> {
                        return recipe.testAddition(stack); // trim/upgrade material
                    });
                }).output(3, 142, 35).build();
        cir.setReturnValue(man);
    }
}
