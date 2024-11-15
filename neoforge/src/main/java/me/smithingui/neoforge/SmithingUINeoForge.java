package me.smithingui.neoforge;

import me.smithingui.SmithingUI;
import net.neoforged.fml.common.Mod;

@Mod(SmithingUI.MOD_ID)
public final class SmithingUINeoForge {
    public SmithingUINeoForge() {
        // Run our common setup.
        SmithingUI.init();
    }
}
