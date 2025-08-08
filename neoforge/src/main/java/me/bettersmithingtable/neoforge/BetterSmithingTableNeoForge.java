package me.bettersmithingtable.neoforge;

import eu.midnightdust.lib.config.MidnightConfig;
import me.bettersmithingtable.BetterSmithingTable;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.ConfigScreenHandler;

@Mod(BetterSmithingTable.MOD_ID)
public final class BetterSmithingTableNeoForge {
    public BetterSmithingTableNeoForge() {
        BetterSmithingTable.init();

        // register config screen
        ModLoadingContext.get().registerExtensionPoint(
                ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory(
                        (client, parent) -> MidnightConfig.getScreen(parent, BetterSmithingTable.MOD_ID)
                )
        );
    }
}
