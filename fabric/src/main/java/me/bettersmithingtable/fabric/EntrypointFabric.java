package me.bettersmithingtable.fabric;

import net.fabricmc.api.ModInitializer;
import me.bettersmithingtable.BetterSmithingTable;

public final class EntrypointFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        BetterSmithingTable.init();
    }
}
