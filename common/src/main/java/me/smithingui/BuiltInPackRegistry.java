package me.smithingui;

import net.minecraft.network.chat.Component;

public interface BuiltInPackRegistry {
    void register(String dir, Component name);
}
