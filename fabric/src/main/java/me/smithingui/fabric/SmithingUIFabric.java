package me.smithingui.fabric;

import me.smithingui.SmithingUI;
import net.fabricmc.api.ModInitializer;

public final class SmithingUIFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        SmithingUI.init();
    }
}
