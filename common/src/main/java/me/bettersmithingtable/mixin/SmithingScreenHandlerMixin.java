package me.bettersmithingtable.mixin;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.recipe.RecipePropertySet;
import net.minecraft.screen.ForgingScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.SmithingScreenHandler;
import net.minecraft.screen.slot.ForgingSlotsManager;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(SmithingScreenHandler.class)
public abstract class SmithingScreenHandlerMixin extends ForgingScreenHandler {
    public SmithingScreenHandlerMixin(@Nullable ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory,
                                      ScreenHandlerContext context, ForgingSlotsManager forgingSlotsManager) {
        super(type, syncId, playerInventory, context, forgingSlotsManager);
    }

    // reposition slots to fit new texture
    @Inject(method = "createForgingSlotsManager", at = @At("HEAD"), cancellable = true)
    private static void createForgingSlotsManager(RecipeManager recipeMgr,
                                                  CallbackInfoReturnable<ForgingSlotsManager> cir) {
        RecipePropertySet template = recipeMgr.getPropertySet(RecipePropertySet.SMITHING_TEMPLATE);
        RecipePropertySet armorPiece = recipeMgr.getPropertySet(RecipePropertySet.SMITHING_BASE);
        RecipePropertySet material = recipeMgr.getPropertySet(RecipePropertySet.SMITHING_ADDITION);
        Objects.requireNonNull(armorPiece);
        Objects.requireNonNull(template);
        Objects.requireNonNull(material);

        ForgingSlotsManager man = ForgingSlotsManager.builder()
                .input(0, 64, 35, template::canUse)
                .input(1, 38, 45, armorPiece::canUse)
                .input(2, 18, 25, material::canUse)
                .output(3, 142, 35).build();

        cir.setReturnValue(man);
    }
}