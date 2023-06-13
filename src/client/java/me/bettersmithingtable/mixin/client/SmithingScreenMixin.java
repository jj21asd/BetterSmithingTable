package me.bettersmithingtable.mixin.client;

import me.bettersmithingtable.BetterSmithingTableClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.CyclingSlotIcon;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.screen.ingame.SmithingScreen;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(SmithingScreen.class)
public abstract class SmithingScreenMixin {
    private static final int ARMOR_STAND_YAW = 200;
    private static final Quaternionf ARMOR_STAND_ROTATION = (new Quaternionf())
            .rotationXYZ(MathHelper.PI * 0.12f, 0, MathHelper.PI);

    /**
     * Redirect titleX assignment in constructor to preserve default values.
     */
    @Redirect(method = "<init>", at = @At(value = "FIELD", opcode = Opcodes.PUTFIELD,
            target = "Lnet/minecraft/client/gui/screen/ingame/SmithingScreen;titleX:I"))

    private void assignTitleX(SmithingScreen instance, int value) {
        // Do nothing.
    }

    /**
     * Redirect titleX assignment in constructor to preserve default values.
     */
    @Redirect(method = "<init>", at = @At(value = "FIELD", opcode = Opcodes.PUTFIELD,
            target = "Lnet/minecraft/client/gui/screen/ingame/SmithingScreen;titleY:I"))

    private void assignTitleY(SmithingScreen instance, int value) {
        // Do nothing
    }

    /**
     * Swap the texture passed into the constructor of ForgingScreen with the updated texture.
     */
    @ModifyArg(method = "<init>", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/gui/screen/ingame/ForgingScreen;<init>(Lnet/minecraft/screen/ForgingScreenHandler;Lnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/text/Text;Lnet/minecraft/util/Identifier;)V"),
            index = 3)

    private static Identifier getTextureInSuperConstructor(Identifier texture) {
        return BetterSmithingTableClient.SMITHING_MENU_LOCATION;
    }

    /**
     * Redirect assignment to customize armor stand yaw value.
     */
    @Redirect(method = "setup", at = @At(value = "FIELD", opcode = Opcodes.PUTFIELD,
            target = "Lnet/minecraft/entity/decoration/ArmorStandEntity;bodyYaw:F"))
    private void assignBodyYaw(ArmorStandEntity instance, float value) {
        instance.bodyYaw = ARMOR_STAND_YAW;
    }

    /**
     * Redirect call to context.drawTexture to draw the invalid recipe arrow at the right position.
     */
    @Redirect(method = "drawInvalidRecipeArrow", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V"))

    private void drawInvalidRecipeArrow(DrawContext instance, Identifier texture, int x, int y, int u, int v, int width, int height) {
        // Get back the original x and y:
        x -= 65;
        y -= 46;

        // Draw the arrow from the new texture at the right location.
        instance.drawTexture(BetterSmithingTableClient.SMITHING_MENU_LOCATION, x + 63, y + 33, u, v, width, height);
    }

    /**
     * Redirect drawSlotTooltip to draw custom tooltips.
     */
    @Redirect(method = "render", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/gui/screen/ingame/SmithingScreen;renderSlotTooltip(Lnet/minecraft/client/gui/DrawContext;II)V"))
    private void renderSLotTooltip(SmithingScreen instance, DrawContext context, int mouseX, int mouseY) {
        // Currently this does nothing to hide all tooltips, but this can be changed in the future.
    }

    /**
     * Redirect these calls and make them do nothing to hide the icons.
     */
    @Redirect(method = "drawBackground", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/gui/screen/ingame/CyclingSlotIcon;render(Lnet/minecraft/screen/ScreenHandler;Lnet/minecraft/client/gui/DrawContext;FII)V"))

    private void renderCyclingSlotIcon(CyclingSlotIcon icon, ScreenHandler screenHandler, DrawContext context, float delta, int x, int y) {
        // Do nothing.
    }

    /**
     * Redirect call to InventoryScreen.drawEntity to draw armor stand with custom rotation and offset.
     */
    @Redirect(method = "drawBackground", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/gui/screen/ingame/InventoryScreen;drawEntity(Lnet/minecraft/client/gui/DrawContext;IIILorg/joml/Quaternionf;Lorg/joml/Quaternionf;Lnet/minecraft/entity/LivingEntity;)V"))

    private void drawArmorStandPreview(DrawContext context, int x, int y, int size, Quaternionf rotation, @Nullable Quaternionf q2, LivingEntity entity) {
        // Little hack to get back the original x and y
        x -= 141;
        y -= 75;

        // Draw the armor stand at its new position:
        InventoryScreen.drawEntity(context, x + 111, y + 67, size, ARMOR_STAND_ROTATION, q2, entity);
    }
}
