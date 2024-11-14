package me.smithingui;

import dev.architectury.hooks.PackRepositoryHooks;
import dev.architectury.platform.Platform;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.ResourceType;
import net.minecraft.resource.pack.*;
import net.minecraft.text.Text;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class BuiltInPackProvider implements ResourcePackProvider {
    private final String rootPath;
    private final List<ResourcePackProfile> profiles;

    public BuiltInPackProvider(String rootPath) {
        this.rootPath = rootPath;
        this.profiles = new ArrayList<>();
    }

    public static void register(BuiltInPackProvider packProvider) {
        ResourcePackManager manager = MinecraftClient.getInstance().getResourcePackManager();
        PackRepositoryHooks.addSource(manager, packProvider);
    }

    public void addPack(String path, Text name) {
        Platform.getMod(SmithingUI.MOD_ID).findResource(rootPath, path).ifPresentOrElse((nioPath) -> {
            ResourcePackProfile pack = ResourcePackProfile.of(
                    SmithingUI.MOD_ID + "/" + path,
                    name,
                    false,
                    id -> new NioResourcePack(id, nioPath, true),
                    ResourceType.CLIENT_RESOURCES,
                    ResourcePackProfile.InsertionPosition.TOP,
                    ResourcePackSource.PACK_SOURCE_BUILTIN
            );

            Objects.requireNonNull(pack);
            profiles.add(pack);
        }, () -> {
            SmithingUI.LOGGER.error("Failed to load included resourcepack: \"{}\"", path);
        });
    }

    @Override
    public void register(Consumer<ResourcePackProfile> profileAdder) {
        for (ResourcePackProfile profile : profiles) {
            profileAdder.accept(profile);
        }
    }
}
