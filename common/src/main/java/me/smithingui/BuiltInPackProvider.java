package me.smithingui;

import dev.architectury.hooks.PackRepositoryHooks;
import dev.architectury.platform.Platform;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.ResourceType;
import net.minecraft.resource.pack.*;
import net.minecraft.text.Text;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class BuiltInPackProvider implements PackProvider {
    private final String rootPath;
    private final List<PackProfile> profiles;

    public BuiltInPackProvider(String rootPath) {
        this.rootPath = rootPath;
        this.profiles = new ArrayList<>();
    }

    public static void register(BuiltInPackProvider packProvider) {
        PackManager manager = MinecraftClient.getInstance().getResourcePackManager();
        PackRepositoryHooks.addSource(manager, packProvider);
    }

    public void addPack(String path, Text name) {
        Platform.getMod(SmithingUI.MOD_ID).findResource(rootPath, path).ifPresentOrElse((nioPath) -> {
            String id = SmithingUI.MOD_ID + "/" + path;

            PackProfile pack = PackProfile.of(id, name, false,
                    new PackFactory(id, nioPath), ResourceType.CLIENT_RESOURCES,
                    PackProfile.InsertionPosition.TOP, PackSource.PACK_SOURCE_BUILTIN);

            Objects.requireNonNull(pack);
            profiles.add(pack);
        }, () -> {
            SmithingUI.LOGGER.error("Failed to load included resourcepack: \"{}\"", path);
        });
    }

    @Override
    public void loadPacks(Consumer<PackProfile> consumer) {
        for (PackProfile profile : profiles) {
            consumer.accept(profile);
        }
    }

    private static class PackFactory implements PackProfile.PackFactory {
        private final String id;
        private final Path path;

        public PackFactory(String id, Path path) {
            this.id = id;
            this.path = path;
        }

        @Override
        public ResourcePack openPrimary(String name) {
            return new NioResourcePack(id, path, true);
        }

        @Override
        public ResourcePack open(String name, PackProfile.Info info) {
            return openPrimary(name); // Idk what this is for
        }
    }
}
