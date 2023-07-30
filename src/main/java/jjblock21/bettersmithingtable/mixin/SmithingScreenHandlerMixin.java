package jjblock21.bettersmithingtable.mixin;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ForgingScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.SmithingScreenHandler;
import net.minecraft.screen.slot.ForgingSlotsManager;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(SmithingScreenHandler.class)
public abstract class SmithingScreenHandlerMixin extends ForgingScreenHandler {

    public SmithingScreenHandlerMixin(@Nullable ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(type, syncId, playerInventory, context);
    }

    private SmithingScreenHandlerAccessor getAccessor() {
        return (SmithingScreenHandlerAccessor)this;
    }

    /**
     * Replace the original slot initialization code to change the positions of the slots.
     * @author jjblock21
     * @reason Change slot positions to fit with the new design.
     */
    @Overwrite
    public ForgingSlotsManager getForgingSlotsManager() {
        return ForgingSlotsManager.create()
                .input(0, 64, 35, this::testTemplateSlot) // Smithing template slot
                .input(1, 38, 45, this::testBaseSlot) // Armor slot
                .input(2, 18, 25, this::testAdditionSlot) // Trim/upgrade material slot
                .output(3, 142, 35).build();
    }

    private boolean testTemplateSlot(ItemStack stack) {
        return getAccessor().getRecipes().stream().anyMatch((recipe) ->
                recipe.testTemplate(stack)
        );
    }

    private boolean testBaseSlot(ItemStack stack) {
        return getAccessor().getRecipes().stream().anyMatch((recipe) ->
                recipe.testBase(stack)
        );
    }

    private boolean testAdditionSlot(ItemStack stack) {
        return getAccessor().getRecipes().stream().anyMatch((recipe) ->
                recipe.testAddition(stack)
        );
    }
}
