package dev.jjblock21.bst.neoforge;

import net.neoforged.fml.loading.FMLLoader;

public class PlatformImpl {
    public static boolean isModLoaded(String id) {
        // use LoadingModList to ensure this works during mixin application
        return FMLLoader
            .getCurrent()
            .getLoadingModList()
            .getModFileById(id) != null;
    }
}
