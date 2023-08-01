package me.bettersmithingtable.forge;

import me.bettersmithingtable.BetterSmithingTable;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(BetterSmithingTable.MOD_ID)
public class BetterSmithingTableForge {
    public BetterSmithingTableForge() {
        BetterSmithingTable.init();
    }
}