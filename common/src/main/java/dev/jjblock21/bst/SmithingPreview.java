package dev.jjblock21.bst;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.entity.state.ArmorStandRenderState;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.jspecify.annotations.NonNull;

public class SmithingPreview {
    private boolean displayingItem = false;
    private boolean dragging = false;
    private float yRot = STAND_YAW;
    private float yRotVelocity = 0;
    private float mouseDeltaXAccum = 0;
    private long lastFrame = -1;

    // transform properties
    public static final float STAND_SIZE = 25;
    public static final float STAND_YAW = 200;
    public static final Vector3f STAND_POS;
    public static final Quaternionf STAND_ROT;

    // dimensions of the rectangle the stand occupies
    public static final int STAND_FRAME_LEFT = 93;
    public static final int STAND_FRAME_TOP = 15;
    public static final int STAND_FRAME_RIGHT = STAND_FRAME_LEFT + 36;
    public static final int STAND_FRAME_BOTTOM = STAND_FRAME_TOP + 56;

    // "rigidity" of movement when dragging/coasting
    public static final float DRAG_LAMBDA = 20;
    public static final float COAST_LAMBDA = 8;

    static {
        STAND_POS = new Vector3f(0, 0.96f, 0);
        STAND_ROT = new Quaternionf().rotationXYZ(Mth.PI * 0.12f, 0, Mth.PI);
    }

    public static void initArmorStand(ArmorStandRenderState armorStand) {
        armorStand.bodyRot = STAND_YAW;
        armorStand.showArms = !BstConfig.armlessArmorStand;
    }

    public void setDisplayItem(ItemStack itemStack) {
        displayingItem = !itemStack.isEmpty();
    }

    // measure delta time, minecraft doesn't provide it
    private float measureDeltaTime() {
        long now = System.nanoTime();
        float dt = 0;
        if (lastFrame >= 0) {
            dt = (float) ((now - lastFrame) / 1e9d);
            dt = Mth.clamp(dt, 0, 0.2f);
        }
        lastFrame = now;
        return dt;
    }

    private void updateArmorStandYaw(float dt) {
        if (!BstConfig.dragToRotate && BstConfig.rotationSpeed == 0) {
            yRotVelocity = 0;
            yRot = STAND_YAW;
            return;
        }

        float lambda, yRotVelocityTarget;
        if (dragging) {
            lambda = DRAG_LAMBDA;
            yRotVelocityTarget = -mouseDeltaXAccum * BstConfig.dragSensitivity / dt;
            mouseDeltaXAccum = 0;
        } else {
            lambda = COAST_LAMBDA;
            yRotVelocityTarget = displayingItem
                ? BstConfig.rotationSpeed * 18
                : 0;
        }

        float decay = (float) Math.exp(-lambda * dt);
        yRotVelocity = yRotVelocityTarget + (yRotVelocity - yRotVelocityTarget) * decay;
        if (Math.abs(yRotVelocity) <= 0.01) {
            yRotVelocity = 0;
        }

        yRot += yRotVelocity * dt;
        yRot = Mth.wrapDegrees(yRot);
    }

    public void extract(GuiGraphicsExtractor graphics, int menuX, int menuY, ArmorStandRenderState armorStand) {
        float dt = measureDeltaTime();
        if (armorStand != null) {
            if (dt > 0) {
                updateArmorStandYaw(dt);
            }

            armorStand.bodyRot = yRot;
            graphics.entity(
                armorStand,
                STAND_SIZE,
                STAND_POS,
                STAND_ROT,
                null,
                menuX + STAND_FRAME_LEFT,
                menuY + STAND_FRAME_TOP,
                menuX + STAND_FRAME_RIGHT,
                menuY + STAND_FRAME_BOTTOM
            );
        }
    }

    private boolean isMouseOver(int x, int y, double mx, double my) {
        return mx >= (x + STAND_FRAME_LEFT)
            && mx < (x + STAND_FRAME_RIGHT)
            && my >= (y + STAND_FRAME_TOP)
            && my < (y + STAND_FRAME_BOTTOM);
    }

    public boolean mouseClicked(@NonNull MouseButtonEvent event, int menuX, int menuY) {
        if (BstConfig.dragToRotate) {
            if (event.button() == 1 && isMouseOver(menuX, menuY, event.x(), event.y())) {
                dragging = true;
                yRotVelocity = 0;
                mouseDeltaXAccum = 0;
                return true;
            }
        }
        return false;
    }

    public boolean mouseDragged(double dx) {
        if (dragging) {
            mouseDeltaXAccum += (float) dx;
            return true;
        }
        return false;
    }

    public boolean mouseReleased(@NonNull MouseButtonEvent event) {
        if (dragging && event.button() == 1) {
            dragging = false;
            return true;
        }
        return false;
    }
}
