package dev.jjblock21.bst.fabric;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.jjblock21.bst.BstMain;
import eu.midnightdust.lib.config.MidnightConfig;

public class BstModMenuImpl implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> MidnightConfig.getScreen(parent, BstMain.MOD_ID);
    }
}
