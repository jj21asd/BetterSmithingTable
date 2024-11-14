package me.smithingui.mixin;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.recipe.SmithingRecipe;
import net.minecraft.screen.ForgingScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.SmithingScreenHandler;
import net.minecraft.screen.slot.ItemCombinationSlotManager;
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


    @Inject(method = "createSlotManager", at = @At("HEAD"), cancellable = true)
    public void createSlotManager(CallbackInfoReturnable<ItemCombinationSlotManager> cir) {
        // Move slots to fit gui
        ItemCombinationSlotManager man = ItemCombinationSlotManager.createBuilder()
                .addIngredientSlot(0, 64, 35, (stack) -> {
                    return this.recipes.stream().anyMatch((recipe) -> {
                        return recipe.matchesTemplateIngredient(stack);
                    });
                }).addIngredientSlot(1, 38, 45, (stack) -> { // Armor piece
                    return this.recipes.stream().anyMatch((recipe) -> {
                        return recipe.matchesBaseIngredient(stack);
                    });
                }).addIngredientSlot(2, 18, 25, (stack) -> { // Trim/Upgrade material
                    return this.recipes.stream().anyMatch((recipe) -> {
                        return recipe.matchesAdditionIngredient(stack);
                    });
                }).setResultSlot(3, 142, 35).build();
        cir.setReturnValue(man);
    }
}
