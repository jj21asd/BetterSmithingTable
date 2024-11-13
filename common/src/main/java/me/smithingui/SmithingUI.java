package me.smithingui;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public final class SmithingUI {
    public static final String MOD_ID = "better_smithing_ui";

    public static void init(BuiltInPackRegistry packRegistry) {
        packRegistry.register("dark_mode", Component.literal("Dark Smithing UI"));
        packRegistry.register("transparent", Component.literal("Transparent Smithing UI"));
    }

    public static ResourceLocation asResource(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
