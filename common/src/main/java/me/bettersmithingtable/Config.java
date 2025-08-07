package me.bettersmithingtable;

import eu.midnightdust.lib.config.MidnightConfig;

public class Config extends MidnightConfig {
    @Entry
    public static Skin skin = Skin.DEFAULT;

    @Entry(min = 0, max = 36, isSlider = true)
    public static int rotationSpeed = 0;

    public enum Skin {
        DEFAULT,
        VT_DARK,
        VT_TRANSPARENT,
    }
}
