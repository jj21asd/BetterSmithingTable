package dev.jjblock21.bst.forge;

import net.minecraftforge.fml.loading.LoadingModList;

public class PlatformImpl {
    public static boolean isModLoaded(String id) {
        // use LoadingModList to ensure this works during mixin application
        return LoadingModList.get().getModFileById(id) != null;
    }
}
