package me.bettersmithingtable;

import net.minecraft.resources.ResourceLocation;
import org.joml.Quaternionf;

public class BetterSmithingTable
{
	public static final String MOD_ID = "bettersmithingtable";

	public static final ResourceLocation SMITHING_MENU_LOCATION = new ResourceLocation(MOD_ID, "menu.png");
	public static final int ARMOR_STAND_YAW = 200;
	public static final Quaternionf ARMOR_STAND_ROTATION = (new Quaternionf())
			.rotationXYZ((float)Math.PI * 0.12f, 0, (float)Math.PI);

	public static void init() { }
}
