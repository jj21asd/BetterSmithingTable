package dev.jjblock21.bst.neoforge;

import dev.jjblock21.bst.BstMain;
import eu.midnightdust.lib.config.MidnightConfig;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(BstMain.MOD_ID)
public final class BstMainNeoForge {
    public BstMainNeoForge() {
        ModLoadingContext.get().registerExtensionPoint(
            IConfigScreenFactory.class,
            () -> (client, parent) -> MidnightConfig.getScreen(parent, BstMain.MOD_ID)
        );
        BstMain.init();
    }
}
