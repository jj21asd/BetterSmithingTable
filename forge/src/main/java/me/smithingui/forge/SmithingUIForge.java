package me.smithingui.forge;

import net.minecraftforge.fml.common.Mod;

import me.smithingui.SmithingUI;

@Mod(SmithingUI.MOD_ID)
public final class SmithingUIForge {
    public SmithingUIForge() {
        SmithingUI.init(); // Run common setup
    }
}
