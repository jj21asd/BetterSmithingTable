package me.bettersmithingtable.fabric;

import net.fabricmc.api.ModInitializer;
import me.bettersmithingtable.BetterSmithingTable;

public final class BetterSmithingTableFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        BetterSmithingTable.init();
    }
}
