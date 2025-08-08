package me.bettersmithingtable.fabric;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import eu.midnightdust.lib.config.MidnightConfig;
import net.fabricmc.api.ModInitializer;

import me.bettersmithingtable.BetterSmithingTable;

public final class BetterSmithingTableFabric implements ModInitializer, ModMenuApi {
    @Override
    public void onInitialize() {
        BetterSmithingTable.init();
    }

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> MidnightConfig.getScreen(parent, BetterSmithingTable.MOD_ID);
    }
}
