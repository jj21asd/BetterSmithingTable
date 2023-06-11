package me.bettersmithingtable;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.ResourceTexture;
import net.minecraft.util.Identifier;

public class BetterSmithingTableClient implements ClientModInitializer {
    public static final Identifier SMITHING_MENU = new Identifier("better_smithing_table", "smithing.png");

    @Override
    public void onInitializeClient() {
        /*ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
            @Override
            public Identifier getFabricId() {
                return new Identifier("assets/better_smithing_table", "my_resources");
            }

            @Override
            public void reload(ResourceManager manager) {
                registerTexture(SMITHING_MENU);
            }
        });*/
    }

    private static void registerTexture(Identifier id) {
        ResourceTexture tex = new ResourceTexture(id);
        MinecraftClient.getInstance().getTextureManager().registerTexture(id, tex);
    }
}