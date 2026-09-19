package dev.jjblock21.bst.fabric;

import dev.jjblock21.bst.BstMain;
import net.fabricmc.api.ModInitializer;

public final class BstMainFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        BstMain.init();
    }
}
