package me.bettersmithingtable.forge;

import eu.midnightdust.lib.config.MidnightConfig;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;

import me.bettersmithingtable.BetterSmithingTable;

@Mod(BetterSmithingTable.MOD_ID)
public final class BetterSmithingTableForge {
    public BetterSmithingTableForge() {
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
