package me.bettersmithingtable.fabric;

import me.bettersmithingtable.BetterSmithingTable;
import net.fabricmc.api.ClientModInitializer;

public class BetterSmithingTableFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BetterSmithingTable.init();
    }
}