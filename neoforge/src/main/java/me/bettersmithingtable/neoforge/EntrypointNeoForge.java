package me.bettersmithingtable.neoforge;

import eu.midnightdust.lib.config.MidnightConfig;
import me.bettersmithingtable.BetterSmithingTable;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(BetterSmithingTable.MOD_ID)
public final class EntrypointNeoForge {
    public EntrypointNeoForge() {
        BetterSmithingTable.init();

        // register config screen
        ModLoadingContext.get().registerExtensionPoint(
                IConfigScreenFactory.class,
                () -> (client, parent) -> MidnightConfig.getScreen(parent, BetterSmithingTable.MOD_ID)
        );
    }
}
