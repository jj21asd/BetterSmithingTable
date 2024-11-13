package me.smithingui.forge;

import me.smithingui.BuiltInPackRegistry;
import me.smithingui.SmithingUI;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.locating.IModFile;
import net.minecraftforge.resource.PathPackResources;
import java.nio.file.Path;
import java.util.Objects;

public class BuiltInPackRegistryForge implements BuiltInPackRegistry {
    private final IModFile modJar;

    public BuiltInPackRegistryForge() {
        modJar = ModList.get().getModFileById(SmithingUI.MOD_ID).getFile();
    }

    @Override
    public void register(String dir, Component name) {
        String packId = SmithingUI.MOD_ID + "_" + dir;
        Path path = modJar.findResource("resourcepacks", dir);

        Pack pack = Pack.readMetaAndCreate(packId, name, false,
                (id) -> new PathPackResources(id, true, path),
                PackType.CLIENT_RESOURCES, Pack.Position.TOP, PackSource.BUILT_IN);

        System.out.println("dfhjlhfuiehfuefhuiehfefuehfefheuih");

        Objects.requireNonNull(pack);
        Minecraft.getInstance().getResourcePackRepository()
                .addPackFinder(consumer -> consumer.accept(pack));
    }
}
