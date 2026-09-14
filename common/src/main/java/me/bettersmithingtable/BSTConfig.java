package me.bettersmithingtable;

import eu.midnightdust.lib.config.MidnightConfig;

public class BSTConfig extends MidnightConfig {
    @Entry public static Skin skin = Skin.DEFAULT;
    @Entry public static boolean dragToRotate = true;
    @Entry(min = 0.1, max = 10) public static float dragSensitivity = 3;
    @Entry(min = -10, max = 10) public static float rotationSpeed = 0;

    public enum Skin {
        DEFAULT,
        VT_DARK,
        VT_TRANSPARENT,
    }
}
