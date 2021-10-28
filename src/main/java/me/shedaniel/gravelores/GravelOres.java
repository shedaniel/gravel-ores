/*
 * This file is part of Gravel Ores.
 *
 * Gravel Ores is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 *
 * Gravel Ores is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Gravel Ores.  If not, see <https://www.gnu.org/licenses/>.
 */
package me.shedaniel.gravelores;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModification;
import net.fabricmc.fabric.api.biome.v1.BiomeModificationContext;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.ConfiguredFeature;

import static net.minecraft.item.ItemGroup.BUILDING_BLOCKS;
import static net.minecraft.util.registry.Registry.CONFIGURED_FEATURE_KEY;

public final class GravelOres implements ModInitializer {
    public static final String MOD_ID = "gravel-ores";
    public static final Item.Settings SETTINGS = new Item.Settings().group(BUILDING_BLOCKS);
    public static final RegistryKey<ConfiguredFeature<?, ?>> COAL_GRAVEL_KEY = RegistryKey.of(CONFIGURED_FEATURE_KEY, register("coal_gravel", 0, new Pair<>(0, 2)));
    public static final RegistryKey<ConfiguredFeature<?, ?>> IRON_GRAVEL_KEY = RegistryKey.of(CONFIGURED_FEATURE_KEY, register("iron_gravel", 1, new Pair<>(1, 3)));
    public static final RegistryKey<ConfiguredFeature<?, ?>> GOLD_GRAVEL_KEY = RegistryKey.of(CONFIGURED_FEATURE_KEY, register("gold_gravel", 2, new Pair<>(2, 6)));
    public static final RegistryKey<ConfiguredFeature<?, ?>> LAPIS_GRAVEL_KEY = RegistryKey.of(CONFIGURED_FEATURE_KEY, register("lapis_gravel", 1, new Pair<>(2, 5)));
    public static final RegistryKey<ConfiguredFeature<?, ?>> REDSTONE_GRAVEL_KEY = RegistryKey.of(CONFIGURED_FEATURE_KEY, register("redstone_gravel", 2, new Pair<>(1, 5)));
    public static final RegistryKey<ConfiguredFeature<?, ?>> EMERALD_GRAVEL_KEY = RegistryKey.of(CONFIGURED_FEATURE_KEY, register("emerald_gravel", 2, new Pair<>(3, 7)));
    public static final RegistryKey<ConfiguredFeature<?, ?>> DIAMOND_GRAVEL_KEY = RegistryKey.of(CONFIGURED_FEATURE_KEY, register("diamond_gravel", 2, new Pair<>(3, 7)));
    public static final RegistryKey<ConfiguredFeature<?, ?>> COPPER_GRAVEL_KEY = RegistryKey.of(CONFIGURED_FEATURE_KEY, register("copper_gravel", 1, new Pair<>(1, 4)));
    
    @Override
    public void onInitialize() {
        Registry.register(Registry.DECORATOR, new Identifier(MOD_ID, "surface_gen"), new SurfaceGenDecorator(SurfaceGenConfig.CODEC));
        Registry.register(Registry.FEATURE, new Identifier(MOD_ID, "surface_gen"), new SurfaceGenFeature(SurfaceGenFeatureConfig.CODEC));
        BiomeModification modification = BiomeModifications.create(new Identifier(MOD_ID, "worldgen"));
        modification.add(ModificationPhase.ADDITIONS, context -> context.getBiome().getCategory() != Biome.Category.NETHER &&
                                                                 context.getBiome().getCategory() != Biome.Category.THEEND, context -> {
            BiomeModificationContext.GenerationSettingsContext generationProperties = context.getGenerationSettings();
            generationProperties.addFeature(GenerationStep.Feature.UNDERGROUND_ORES, COAL_GRAVEL_KEY);
            generationProperties.addFeature(GenerationStep.Feature.UNDERGROUND_ORES, IRON_GRAVEL_KEY);
            generationProperties.addFeature(GenerationStep.Feature.UNDERGROUND_ORES, GOLD_GRAVEL_KEY);
            generationProperties.addFeature(GenerationStep.Feature.UNDERGROUND_ORES, LAPIS_GRAVEL_KEY);
            generationProperties.addFeature(GenerationStep.Feature.UNDERGROUND_ORES, REDSTONE_GRAVEL_KEY);
            generationProperties.addFeature(GenerationStep.Feature.UNDERGROUND_ORES, DIAMOND_GRAVEL_KEY);
            generationProperties.addFeature(GenerationStep.Feature.UNDERGROUND_ORES, COPPER_GRAVEL_KEY);
        });
        modification.add(ModificationPhase.ADDITIONS, context -> context.getBiome().getCategory() == Biome.Category.EXTREME_HILLS, context -> {
            context.getGenerationSettings().addFeature(GenerationStep.Feature.UNDERGROUND_ORES, EMERALD_GRAVEL_KEY);
        });
    }
    
    private static Identifier register(final String name, final int miningLevel, final Pair<Integer, Integer> xpLimits) {
        final Identifier identifier = new Identifier(MOD_ID, name);
        Registry.register(Registry.ITEM, identifier, new BlockItem(Registry.register(Registry.BLOCK, identifier, new GravelOreBlock(miningLevel, xpLimits)), SETTINGS));
        return identifier;
    }
}
