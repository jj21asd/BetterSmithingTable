package me.smithingui.fabric;

import me.smithingui.SmithingUI;
import me.smithingui.BuiltInPackRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.chat.Component;

public final class BuiltInPackRegistryFabric implements BuiltInPackRegistry {
    @Override
    public void register(String fileName, Component name) {
        FabricLoader.getInstance().getModContainer(SmithingUI.MOD_ID).ifPresent(container -> {
            ResourceManagerHelper.registerBuiltinResourcePack(SmithingUI.asResource(fileName),
                    container, name, ResourcePackActivationType.NORMAL);
        });
    }
}
