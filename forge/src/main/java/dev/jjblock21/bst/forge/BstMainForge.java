package dev.jjblock21.bst.forge;

import dev.jjblock21.bst.BstMain;
import eu.midnightdust.lib.config.MidnightConfig;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(BstMain.MOD_ID)
public final class BstMainForge {
    public BstMainForge(FMLJavaModLoadingContext context) {
        context.registerExtensionPoint(
            ConfigScreenHandler.ConfigScreenFactory.class,
            () -> new ConfigScreenHandler.ConfigScreenFactory(
                (client, parent) -> MidnightConfig.getScreen(parent, BstMain.MOD_ID)
            )
        );
        BstMain.init();
    }
}
