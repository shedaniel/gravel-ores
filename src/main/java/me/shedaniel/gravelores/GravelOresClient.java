package me.shedaniel.gravelores;

import com.swordglowsblue.artifice.api.Artifice;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.commons.lang3.text.WordUtils;

public class GravelOresClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        for (Block block : Registry.BLOCK) {
            handleBlock(Registry.BLOCK.getId(block), block);
        }
        RegistryEntryAddedCallback.event(Registry.BLOCK).register((rawId, identifier, block) -> handleBlock(identifier, block));
        Artifice.registerAssets("gravel-ores:gravel-ores-assets", pack -> {
            pack.setDisplayName("Gravel Ores Assets");
            pack.addTranslations(new Identifier("en_us"), translations -> {
                GravelOres.REGISTERED_GRAVELS.forEach((material, identifier) -> {
                    translations.entry("block." + identifier.getNamespace() + "." + identifier.getPath(), WordUtils.capitalize(identifier.getPath().replace('_', ' ')));
                });
            });
            GravelOres.REGISTERED_GRAVELS.forEach((material, identifier) -> {
                pack.addItemModel(identifier, model -> {
                    model.parent(new Identifier(identifier.getNamespace(), "block/" + identifier.getPath()));
                });
                pack.addBlockState(identifier, blockState -> {
                    blockState.variant("", variant -> {
                        variant.model(new Identifier(identifier.getNamespace(), "block/" + identifier.getPath()));
                    });
                });
                pack.addBlockModel(identifier, model -> {
                    Identifier gravelId = Registry.BLOCK.getId(Blocks.GRAVEL);
                    model.parent(new Identifier("gravel-ores", "block/ore"))
                            .texture("base", new Identifier(gravelId.getNamespace(), "block/" + gravelId.getPath()));
                    if (GravelOres.getGravelInformation(identifier).isCottonResource()) {
                        model.texture("overlay", new Identifier("c", "block/" + material + "_ore"));
                    } else {
                        model.texture("overlay", new Identifier(identifier.getNamespace(), "block/" + identifier.getPath() + "_overlay"));
                    }
                });
            });
        });
    }
    
    private void handleBlock(Identifier identifier, Block block) {
        if (GravelOres.REGISTERED_GRAVELS.containsValue(identifier)) {
            BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getCutoutMipped());
        }
    }
}
