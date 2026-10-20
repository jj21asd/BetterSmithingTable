package dev.jjblock21.bst.mixin;

import dev.jjblock21.bst.core.SmithingPreview;
import dev.jjblock21.bst.core.SmithingPreviewHolder;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
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
    private void mouseClicked(double mx, double my, int button, CallbackInfoReturnable<Boolean> cir) {
        if ((Object) this instanceof SmithingPreviewHolder holder) {
            SmithingPreview preview = holder.bst$getSmithingPreview();
            if (preview.mouseClicked(leftPos, topPos, mx, my, button)) {
                cir.setReturnValue(true);
            }
        }
    }

    @Inject(method = "mouseDragged", at = @At("HEAD"), cancellable = true)
    private void mouseDragged(double mx, double my, int button, double dx, double dy,
                              CallbackInfoReturnable<Boolean> cir) {
        if ((Object) this instanceof SmithingPreviewHolder holder) {
            SmithingPreview preview = holder.bst$getSmithingPreview();
            if (preview.mouseDragged(mx, my, button, dx, dy)) {
                cir.setReturnValue(true);
            }
        }
    }

    @Inject(method = "mouseReleased", at = @At("HEAD"), cancellable = true)
    private void mouseReleased(double mx, double my, int button, CallbackInfoReturnable<Boolean> cir) {
        if ((Object) this instanceof SmithingPreviewHolder holder) {
            SmithingPreview preview = holder.bst$getSmithingPreview();
            if (preview.mouseReleased(mx, my, button)) {
                cir.setReturnValue(true);
            }
        }
    }
}
