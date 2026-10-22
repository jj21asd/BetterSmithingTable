package dev.jjblock21.bst.mixin;

import dev.jjblock21.bst.core.SmithingPreview;
import dev.jjblock21.bst.core.SmithingPreviewHolder;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.input.MouseButtonEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractContainerScreen.class)
public class MixinAbstractContainerScreen {
    @Shadow
    protected int leftPos;
    @Shadow
    protected int topPos;

    // inject into the mouse events from here instead of using @Override in MixinSmithingScreen
    // because other mods might do the same and cause things to break
    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void mouseClicked(MouseButtonEvent event, boolean doubleClick, CallbackInfoReturnable<Boolean> cir) {
        if ((Object) this instanceof SmithingPreviewHolder holder) {
            SmithingPreview preview = holder.bst$getSmithingPreview();
            if (preview.mouseClicked(event, leftPos, topPos)) {
                cir.setReturnValue(true);
            }
        }
    }

    @Inject(method = "mouseDragged", at = @At("HEAD"), cancellable = true)
    private void mouseDragged(MouseButtonEvent event, double dx, double dy, CallbackInfoReturnable<Boolean> cir) {
        if ((Object) this instanceof SmithingPreviewHolder holder) {
            SmithingPreview preview = holder.bst$getSmithingPreview();
            if (preview.mouseDragged(dx)) {
                cir.setReturnValue(true);
            }
        }
    }

    @Inject(method = "mouseReleased", at = @At("HEAD"), cancellable = true)
    private void mouseReleased(MouseButtonEvent event, CallbackInfoReturnable<Boolean> cir) {
        if ((Object) this instanceof SmithingPreviewHolder holder) {
            SmithingPreview preview = holder.bst$getSmithingPreview();
            if (preview.mouseReleased(event)) {
                cir.setReturnValue(true);
            }
        }
    }
}
