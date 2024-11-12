package me.smithingui.fabric;

import net.fabricmc.api.ModInitializer;

import me.smithingui.SmithingUI;

public final class SmithingUIFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        SmithingUI.init(); // Run common setup
    }
}
