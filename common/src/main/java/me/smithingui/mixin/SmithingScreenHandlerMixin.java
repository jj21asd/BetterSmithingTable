package me.smithingui.mixin;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeEntry;
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
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.List;

@Mixin(SmithingScreenHandler.class)
public abstract class SmithingScreenHandlerMixin extends ForgingScreenHandler {
    @Shadow @Final
    private List<RecipeEntry<SmithingRecipe>> recipes;

    public SmithingScreenHandlerMixin(@Nullable ScreenHandlerType<?> type, int syncId,
                                      PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(type, syncId, playerInventory, context);
    }

    @Inject(method = "getForgingSlotsManager", at = @At("HEAD"), cancellable = true)
    public void createSlotManager(CallbackInfoReturnable<ForgingSlotsManager> cir) {
        // Move slots to fit gui
        ForgingSlotsManager man = ForgingSlotsManager.create()
                .input(0, 64, 35, this::better_smithing_ui$testTemplate)
                .input(1, 38, 45, this::better_smithing_ui$testArmorPiece)
                .input(2, 18, 25, this::better_smithing_ui$testUpgradeMaterial)
                .output(3, 142, 35).build();

        cir.setReturnValue(man);
    }

    @Unique
    private boolean better_smithing_ui$testTemplate(ItemStack stack) {
        return this.recipes.stream().anyMatch((recipe) -> {
            return recipe.value().testTemplate(stack);
        });
    }

    @Unique
    private boolean better_smithing_ui$testArmorPiece(ItemStack stack) {
        return this.recipes.stream().anyMatch((recipe) -> {
            return recipe.value().testBase(stack);
        });
    }

    @Unique
    private boolean better_smithing_ui$testUpgradeMaterial(ItemStack stack) {
        return this.recipes.stream().anyMatch((recipe) -> {
            return recipe.value().testAddition(stack);
        });
    }
}
