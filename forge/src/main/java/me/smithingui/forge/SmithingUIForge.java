package me.smithingui.forge;

import dev.architectury.platform.forge.EventBuses;
import me.smithingui.SmithingUI;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(SmithingUI.MOD_ID)
public final class SmithingUIForge {
    public SmithingUIForge() {
        EventBuses.registerModEventBus(SmithingUI.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        SmithingUI.init();
    }
}
