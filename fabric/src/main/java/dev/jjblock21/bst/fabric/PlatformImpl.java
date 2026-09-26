package dev.jjblock21.bst.fabric;

import net.fabricmc.loader.api.FabricLoader;

public class PlatformImpl {
    public static boolean isModLoaded(String id) {
        return FabricLoader.getInstance().isModLoaded(id);
    }
}
