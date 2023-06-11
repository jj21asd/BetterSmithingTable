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
    private static final Quaternionf ARMOR_STAND_ROTATION = (new Quaternionf())
            .rotationXYZ(MathHelper.PI * 0.05f, 0, MathHelper.PI);

    /**
     * Redirect titleX assignment in constructor to do nothing to preserve default values.
     */
    @Redirect(method = "<init>", at = @At(value = "FIELD", opcode = Opcodes.PUTFIELD,
            target = "Lnet/minecraft/client/gui/screen/ingame/SmithingScreen;titleX:I"))

    private void assignTitleX(SmithingScreen instance, int value) {
        // Do nothing.
    }

    /**
     * Redirect titleX assignment in constructor to do nothing to preserve default values.
     */
    @Redirect(method = "<init>", at = @At(value = "FIELD", opcode = Opcodes.PUTFIELD,
            target = "Lnet/minecraft/client/gui/screen/ingame/SmithingScreen;titleY:I"))

    private void assignTitleY(SmithingScreen instance, int value) {
        // Do nothing
    }

    /**
     * Inject a method to modify the texture passed into the constructor of ForgingScreen.
     */
    @ModifyArg(method = "<init>", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/gui/screen/ingame/ForgingScreen;<init>(Lnet/minecraft/screen/ForgingScreenHandler;Lnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/text/Text;Lnet/minecraft/util/Identifier;)V"),
            index = 3)

    private static Identifier getTextureInSuperConstructor(Identifier texture) {
        return BetterSmithingTableClient.SMITHING_MENU;
    }


    /*@Redirect(method = "setup", at = @At(value = "FIELD", opcode = Opcodes.PUTFIELD,
            target = "Lnet/minecraft/entity/decoration/ArmorStandEntity;bodyYaw:F"))
    private void assignBodyYaw(ArmorStandEntity instance, float value) {
        // Here one would customize the armor stands yaw.
    }*/

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

        // Draw the armor stand at ts new position (in this case 143, 66)
        InventoryScreen.drawEntity(context, x + 143, y + 67, size, ARMOR_STAND_ROTATION, q2, entity);
    }
}
