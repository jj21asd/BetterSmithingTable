package me.bettersmithingtable;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.util.math.MathHelper;
import org.joml.Quaternionf;

public class SmithingPreviewRenderer {
    private boolean presentingItem;
    private boolean dragging;
    private long lastNanos;
    private float mouseDeltaX;
    private float yaw, yawVel;

    private static final int STAND_X = 111;
    private static final int STAND_Y = 67;
    private static final float STAND_YAW = 200;
    private static final Quaternionf STAND_ROT;

    private static final int STAND_FRAME_X = 93;
    private static final int STAND_FRAME_Y = 15;
    private static final int STAND_FRAME_WIDTH = 36;
    private static final int STAND_FRAME_HEIGHT = 56;

    private static final float MAX_DELTA_TIME = 1;
    private static final float DRAG_LAMBDA = 20;
    private static final float COAST_LAMBDA = 8;

    static {
        STAND_ROT = new Quaternionf()
            .rotationXYZ(MathHelper.PI * 0.12f, 0, MathHelper.PI);
    }

    public SmithingPreviewRenderer() {
        presentingItem = false;
        dragging = false;
        lastNanos = -1;
        mouseDeltaX = 0;
        yaw = STAND_YAW;
        yawVel = 0;
    }

    public void setPresentingItem(boolean value) {
        presentingItem = value;
    }

    private boolean isMouseOver(int x, int y, double mx, double my) {
        int l = x + STAND_FRAME_X;
        int t = y + STAND_FRAME_Y;
        int r = l + STAND_FRAME_WIDTH;
        int b = t + STAND_FRAME_HEIGHT;
        return mx >= l && mx < r && my >= t && my < b;
    }

    public boolean handleClick(int x, int y, double mx, double my, int button) {
        if (BSTConfig.dragToRotate && button == 0 && isMouseOver(x, y, mx, my)) {
            dragging = true;
            return true;
        }
        return false;
    }

    public boolean handleDrag(double dx) {
        if (dragging) {
            mouseDeltaX += (float)dx;
            return true;
        }
        return false;
    }

    public boolean handleRelease(int button) {
        if (dragging && button == 0) {
            dragging = false;
            return true;
        }
        return false;
    }

    private float sampleDeltaTime() {
        // Minecraft doesn't provide delta time, we have to measure it ourselves
        long now = System.nanoTime();
        double dt = 0;
        if (lastNanos >= 0) {
            dt = (double)(now - lastNanos) / 1e9;
            if (dt < 0 || dt > MAX_DELTA_TIME) {
                dt = 0; // Prevent weirdness
            }
        }
        lastNanos = now;
        return (float)dt;
    }

    private void update() {
        if (!BSTConfig.dragToRotate) {
            yawVel = 0;
            yaw = STAND_YAW;
            return;
        }

        float dt = sampleDeltaTime();
        if (dt > 0) {
            float target, lambda;
            if (dragging) {
                target = -mouseDeltaX * BSTConfig.dragSensitivity / dt;
                mouseDeltaX = 0;
                lambda = DRAG_LAMBDA;
            } else {
                // Counter-clockwise looks better I think
                target = presentingItem ? -BSTConfig.rotationSpeed * 18 : 0;
                lambda = COAST_LAMBDA;
            }

            float decay = (float) Math.exp(-lambda * dt);
            yawVel = target + (yawVel - target) * decay;
            yaw += yawVel * dt;
            yaw = MathHelper.wrapDegrees(yaw);
        }
    }

    public void draw(DrawContext context, int x, int y, ArmorStandEntity armorStand) {
        update();
        armorStand.bodyYaw = yaw;
        InventoryScreen.drawEntity(
            context,
            x + STAND_X,
            y + STAND_Y,
            25,
            STAND_ROT,
            new Quaternionf(),
            armorStand
        );
    }
}
