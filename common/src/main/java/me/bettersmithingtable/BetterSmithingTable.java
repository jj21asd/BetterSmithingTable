package me.bettersmithingtable;

import eu.midnightdust.lib.config.MidnightConfig;
import net.minecraft.util.Identifier;

public class BetterSmithingTable {
    public static final String MOD_ID = "better_smithing_table";

    public static void init() {
        MidnightConfig.init(MOD_ID, Config.class);
    }

    public static Identifier getMenuTexture() {
        return new Identifier(MOD_ID, switch (Config.skin) {
            case DEFAULT -> "menu.png";
            case VT_DARK -> "menu_vt_dark.png";
            case VT_TRANSPARENT -> "menu_vt_transparent.png";
        });
    }
}
