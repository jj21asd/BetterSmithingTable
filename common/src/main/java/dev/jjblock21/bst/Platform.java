package dev.jjblock21.bst;

import dev.architectury.injectables.annotations.ExpectPlatform;

public class Platform {
    @ExpectPlatform
    public static boolean isModLoaded(String id) {
        throw new AssertionError();
    }
}
