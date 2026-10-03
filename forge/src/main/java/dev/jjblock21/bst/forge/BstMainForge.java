package dev.jjblock21.bst.forge;

import dev.jjblock21.bst.BstMain;
import eu.midnightdust.lib.config.MidnightConfig;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;

@Mod(BstMain.MOD_ID)
public final class BstMainForge {
    // ModLoadingContext.get() still works and is required here
    @SuppressWarnings("removal")
    public BstMainForge() {
        ModLoadingContext.get().registerExtensionPoint(
            ConfigScreenHandler.ConfigScreenFactory.class,
            () -> new ConfigScreenHandler.ConfigScreenFactory(
                (client, parent) -> MidnightConfig.getScreen(parent, BstMain.MOD_ID)
            )
        );
        BstMain.init();
    }
}
