package dev.jjblock21.bst;

import eu.midnightdust.lib.config.MidnightConfig;

public final class BstMain {
    public static final String MOD_ID = "better_smithing_table";

    public static void init() {
        MidnightConfig.init(MOD_ID, BstConfig.class);
    }
}
