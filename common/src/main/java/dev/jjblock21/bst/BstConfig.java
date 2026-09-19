package dev.jjblock21.bst;

import eu.midnightdust.lib.config.MidnightConfig;

public class BstConfig extends MidnightConfig {
    @Entry
    public static boolean dragToRotate = true;

    @Entry(min = 0, max = 10)
    @Condition(
        requiredOption = BstMain.MOD_ID + ":dragToRotate",
        visibleButLocked = true
    )
    public static float dragSensitivity = 3;

    @Entry(min = -10, max = 10, isSlider = true)
    public static int rotationSpeed = 0;

    @Entry
    public static boolean armlessArmorStand = false;
}
