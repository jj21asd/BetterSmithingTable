package me.bettersmithingtable.fabric;

import com.terraformersmc.modmenu.api.ModMenuApi;
import net.fabricmc.api.ModInitializer;

import me.bettersmithingtable.BetterSmithingTable;

public final class BetterSmithingTableFabric implements ModInitializer, ModMenuApi {
    @Override
    public void onInitialize() {
        BetterSmithingTable.init();
    }
}
