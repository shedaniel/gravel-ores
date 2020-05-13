package me.shedaniel.gravelores;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.swordglowsblue.artifice.api.Artifice;
import com.swordglowsblue.artifice.api.resource.StringResource;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricMaterialBuilder;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.fabricmc.fabric.impl.tool.attribute.ToolManagerImpl;
import net.fabricmc.fabric.mixin.object.builder.BlockAccessor;
import net.fabricmc.fabric.mixin.object.builder.BlockSettingsAccessor;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.loot.ConstantLootTableRange;
import net.minecraft.loot.LootManager;
import net.minecraft.loot.UniformLootTableRange;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.feature.Feature;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GravelOres implements ModInitializer {
    public static final Logger LOGGER = LogManager.getLogger("GravelOres");
    private static final Material SAND_REQUIRE_TOOL = new FabricMaterialBuilder(MaterialColor.SAND).requiresTool().build();
    public static final Map<String, Identifier> REGISTERED_GRAVELS = new LinkedHashMap<>();
    public static final Map<Identifier, OreInformation> DEFAULT_INFO = Maps.newHashMap();
    public static final Decorator<SurfaceGenConfig> SURFACE_GEN_DECORATOR = Registry.register(Registry.DECORATOR, new Identifier("gravel-ores", "surface_gen_config"), new SurfaceGenDecorator(SurfaceGenConfig::deserialize));
    public static final Feature<SurfaceGenFeatureConfig> SURFACE_GEN = Registry.register(Registry.FEATURE, new Identifier("gravel-ores", "surface_gen"), new SurfaceGenFeature(SurfaceGenFeatureConfig::deserialize));
    public static final List<Function<Identifier, OreInformation>> INFO_PROVIDER = new ArrayList<>();
    
    static {
        INFO_PROVIDER.add(DEFAULT_INFO::get);
        register("coal", new OreInformation(new Identifier("coal"), new ConstantLootTableRange(1), new Pair<>(0, 2), new SurfaceGenConfig(6, 0.007f, 0.4f)).generateRecipes());
        register("iron", new OreInformation(new Identifier("iron_ingot"), new SurfaceGenConfig(6, 0.006f, 0.4f)).generateRecipes());
        register("gold", new OreInformation(new Identifier("gold_ingot"), new SurfaceGenConfig(6, 0.003f, 0.6f)).generateRecipes());
        register("redstone", new OreInformation(new Identifier("redstone"), new UniformLootTableRange(4, 5), new Pair<>(1, 6), new SurfaceGenConfig(6, 0.008f, 0.5f)).generateRecipes());
        register("emerald", new OreInformation(new Identifier("emerald"), new ConstantLootTableRange(1), new Pair<>(3, 7), OreInformation.EMPTY_SURFACE_GEN_CONFIG).generateRecipes());
        register("lapis", new OreInformation(new Identifier("lapis_lazuli"), new UniformLootTableRange(4, 9), new Pair<>(2, 5), new SurfaceGenConfig(6, 0.004f, 0.5f)).generateRecipes());
        register("diamond", new OreInformation(new Identifier("diamond"), new ConstantLootTableRange(1), new Pair<>(3, 7), new SurfaceGenConfig(2, 0.002f, 0.6f)).generateRecipes());
        if (FabricLoader.getInstance().isModLoaded("cotton-resources")) {
            registerCottonResource("aluminum", new OreInformation(new SurfaceGenConfig(3, 0.007f, 0.6f)));
            registerCottonResource("copper", new OreInformation(new SurfaceGenConfig(5, 0.004f, 0.4f)));
            registerCottonResource("iridium", new OreInformation(new SurfaceGenConfig(2, 0.003f, 0.6f)));
            registerCottonResource("lead", new OreInformation(new SurfaceGenConfig(2, 0.005f, 0.5f)));
            registerCottonResource("osmium", new OreInformation(new SurfaceGenConfig(2, 0.0025f, 0.6f)));
            registerCottonResource("palladium", new OreInformation(new SurfaceGenConfig(2, 0.0025f, 0.6f)));
            registerCottonResource("platinum", new OreInformation(new SurfaceGenConfig(2, 0.0025f, 0.6f)));
            registerCottonResource("silver", new OreInformation(new SurfaceGenConfig(2, 0.005f, 0.5f)));
            registerCottonResource("tin", new OreInformation(new SurfaceGenConfig(3, 0.007f, 0.5f)));
            registerCottonResource("titanium", new OreInformation(new SurfaceGenConfig(2, 0.005f, 0.75f)));
            registerCottonResource("tungsten", new OreInformation(new SurfaceGenConfig(2, 0.005f, 0.5f)));
            registerCottonResource("uranium", new OreInformation(new SurfaceGenConfig(2, 0.005f, 0.75f)));
            registerCottonResource("zinc", new OreInformation(new SurfaceGenConfig(3, 0.007f, 0.5f)));
            registerCottonResource("ruby", new OreInformation(new Identifier("c", "ruby"), new ConstantLootTableRange(1), OreInformation.EMPTY_XP, new SurfaceGenConfig(1, 0.005f, 0.7f)));
            registerCottonResource("topaz", new OreInformation(new Identifier("c", "topaz"), new ConstantLootTableRange(1), OreInformation.EMPTY_XP, new SurfaceGenConfig(1, 0.005f, 0.7f)));
            registerCottonResource("sapphire", new OreInformation(new Identifier("c", "sapphire"), new ConstantLootTableRange(1), OreInformation.EMPTY_XP, new SurfaceGenConfig(1, 0.005f, 0.7f)));
            registerCottonResource("peridot", new OreInformation(new Identifier("c", "peridot"), new ConstantLootTableRange(1), OreInformation.EMPTY_XP, new SurfaceGenConfig(1, 0.005f, 0.7f)));
            registerCottonResource("amethyst", new OreInformation(new Identifier("c", "amethyst"), new ConstantLootTableRange(1), OreInformation.EMPTY_XP, new SurfaceGenConfig(1, 0.005f, 0.7f)));
        }
    }
    
    private static void register(String material, OreInformation information) {
        DEFAULT_INFO.put(new Identifier("gravel-ores", material + "_gravel"), information);
    }
    
    private static void registerCottonResource(String material, OreInformation information) {
        information.setCottonResource(true);
        register(material, information);
    }
    
    public static OreInformation getGravelInformation(Identifier identifier) {
        for (Function<Identifier, OreInformation> function : INFO_PROVIDER) {
            OreInformation information = function.apply(identifier);
            if (information != null) {
                return information;
            }
        }
        return null;
    }
    
    @Override
    public void onInitialize() {
        for (Block block : Registry.BLOCK) handleBlock(Registry.BLOCK.getId(block), block, false);
        RegistryEntryAddedCallback.event(Registry.BLOCK).register((rawId, identifier, block) -> handleBlock(identifier, block, true));
        for (Biome biome : Registry.BIOME) handleBiome(biome);
        RegistryEntryAddedCallback.event(Registry.BIOME).register((rawId, identifier, biome) -> handleBiome(biome));
        if (FabricLoader.getInstance().isModLoaded("cotton-resources")) {
            Artifice.registerData(new Identifier("gravel-ores:gravel-ores-cotton-resources-data"), pack -> {
                pack.setDisplayName("Gravel Ores Cotton Resources Data");
                String[] cottonResources = new String[]{"copper", "silver", "lead", "zinc", "aluminum", "cobalt", "tin", "titanium", "tungsten", "platinum", "palladium", "osmium", "iridium", "coal_coke", "uranium", "ruby", "topaz", "amethyst", "peridot", "sapphire"};
                pack.add(new Identifier("gravel-ores", "oregen/enable.json"), new StringResource("{\"ores\":[" + Stream.of(cottonResources).map(s -> "\"" + s + "\"").collect(Collectors.joining(",")) + "]}"));
            });
        }
    }
    
    private void handleBiome(Biome biome) {
        REGISTERED_GRAVELS.forEach((material, identifier) -> handleBiomeForGravel(biome, material, identifier));
    }
    
    private void handleBiomeForGravel(Biome biome, String material, Identifier identifier) {
        OreInformation information = getGravelInformation(identifier);
        if (information.getSurfaceGen() == OreInformation.EMPTY_SURFACE_GEN_CONFIG) return;
        biome.addFeature(GenerationStep.Feature.LOCAL_MODIFICATIONS,
                SURFACE_GEN.configure(new SurfaceGenFeatureConfig(Registry.BLOCK.get(identifier).getDefaultState(), 0, Blocks.GRAVEL.getDefaultState(), information.getSurfaceGen().basePossibility))
                        .createDecoratedFeature(SURFACE_GEN_DECORATOR.configure(information.getSurfaceGen())));
    }
    
    private void handleBlock(Identifier identifier, Block block, boolean after) {
        if (identifier.getPath().endsWith("_ore")) {
            String material = identifier.getPath().substring(0, identifier.getPath().length() - 4);
            Identifier gravelId = new Identifier("gravel-ores", material + "_gravel");
            if (REGISTERED_GRAVELS.containsKey(material)) return;
            OreInformation information = getGravelInformation(gravelId);
            if (information != null) {
                if (identifier.getNamespace().equals("c") || identifier.getNamespace().equals("minecraft")) {
                    int miningLevel = getMiningLevel(block);
                    Block gravelBlock = new GravelOreBlock(
                            copyOfWithMaterial(SAND_REQUIRE_TOOL, MaterialColor.STONE, block)
                                    .sounds(BlockSoundGroup.GRAVEL)
                                    .breakByTool(FabricToolTags.SHOVELS, miningLevel),
                            information.getXp()
                    );
                    Item blockItem = new BlockItem(gravelBlock, new Item.Settings().group(ItemGroup.BUILDING_BLOCKS));
                    Registry.register(Registry.BLOCK, gravelId, gravelBlock);
                    Registry.register(Registry.ITEM, gravelId, blockItem);
                    REGISTERED_GRAVELS.put(material, gravelId);
                    LOGGER.info("Registered Gravel: " + gravelId + " from " + identifier + "@" + block.getClass().toString());
                    if (after) {
                        for (Biome biome : Registry.BIOME) {
                            handleBiomeForGravel(biome, material, gravelId);
                        }
                    }
                    addGravelData(gravelId, material, information);
                }
            }
        }
    }
    
    private void addGravelData(Identifier identifier, String material, OreInformation information) {
        Artifice.registerData(new Identifier(identifier.toString() + "_data"), pack -> {
            pack.setDisplayName("Gravel Ores: " + WordUtils.capitalize(identifier.getPath().replace('_', ' ')));
            if (information.isRegisterRecipes()) {
                pack.addSmeltingRecipe(new Identifier(identifier.getNamespace(), material + "_from_smelting_gravel"), recipe -> {
                    recipe.experience(1.0);
                    recipe.cookingTime(200);
                    recipe.result(information.getDrop());
                    recipe.ingredientItem(identifier);
                });
                pack.addBlastingRecipe(new Identifier(identifier.getNamespace(), material + "_from_blasting_gravel"), recipe -> {
                    recipe.experience(1.0);
                    recipe.cookingTime(100);
                    recipe.result(information.getDrop());
                    recipe.ingredientItem(identifier);
                });
            }
            pack.addBlockTag(new Identifier("enderman_holdable"), tag -> {
                tag.value(identifier).replace(false);
            });
            pack.addBlockTag(new Identifier("bamboo_plantable_on"), tag -> {
                tag.value(identifier).replace(false);
            });
            pack.addBlockTag(new Identifier("c", material + "_ore"), tag -> {
                tag.value(identifier).replace(false);
            });
            pack.addBlockTag(new Identifier("c", material + "_ores"), tag -> {
                tag.value(identifier).replace(false);
            });
            pack.addItemTag(new Identifier("c", material + "_ore"), tag -> {
                tag.value(identifier).replace(false);
            });
            pack.addItemTag(new Identifier("c", material + "_ores"), tag -> {
                tag.value(identifier).replace(false);
            });
            if (information.isDropOre()) {
                pack.addLootTable(new Identifier(identifier.getNamespace(), "blocks/" + identifier.getPath()), builder -> {
                    builder.type(new Identifier("block")).pool(pool -> {
                        pool.rolls(1)
                                .condition(new Identifier("survives_explosion"), objBuilder -> {})
                                .entry(entry -> {
                                    entry.type(new Identifier("item"))
                                            .name(identifier);
                                });
                    });
                });
            } else {
                String gravel = identifier.toString();
                String dropItem = information.getDrop().toString();
                String possibleCount;
                try {
                    Field field = LootManager.class.getDeclaredField(FabricLoader.getInstance().getMappingResolver().mapFieldName("intermediary", "net.minecraft.class_60", "field_974", "Lcom/google/gson/Gson;"));
                    field.setAccessible(true);
                    Gson gson = (Gson) field.get(null);
                    possibleCount = "{\"function\":\"minecraft:set_count\",\"count\":" + gson.toJson(information.getDropCount()) + "},";
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                String json = String.format(
                        "{\n" +
                        "  \"type\": \"minecraft:block\",\n" +
                        "  \"pools\": [\n" +
                        "    {\n" +
                        "      \"rolls\": 1,\n" +
                        "      \"entries\": [\n" +
                        "        {\n" +
                        "          \"type\": \"minecraft:alternatives\",\n" +
                        "          \"children\": [\n" +
                        "            {\n" +
                        "              \"type\": \"minecraft:item\",\n" +
                        "              \"conditions\": [\n" +
                        "                {\n" +
                        "                  \"condition\": \"minecraft:match_tool\",\n" +
                        "                  \"predicate\": {\n" +
                        "                    \"enchantments\": [\n" +
                        "                      {\n" +
                        "                        \"enchantment\": \"minecraft:silk_touch\",\n" +
                        "                        \"levels\": {\n" +
                        "                          \"min\": 1\n" +
                        "                        }\n" +
                        "                      }\n" +
                        "                    ]\n" +
                        "                  }\n" +
                        "                }\n" +
                        "              ],\n" +
                        "              \"name\": \"%s\"\n" +
                        "            },\n" +
                        "            {\n" +
                        "              \"type\": \"minecraft:item\",\n" +
                        "              \"functions\": [\n" +
                        possibleCount +
                        "                {\n" +
                        "                  \"function\": \"minecraft:apply_bonus\",\n" +
                        "                  \"enchantment\": \"minecraft:fortune\",\n" +
                        "                  \"formula\": \"minecraft:ore_drops\"\n" +
                        "                },\n" +
                        "                {\n" +
                        "                  \"function\": \"minecraft:explosion_decay\"\n" +
                        "                }\n" +
                        "              ],\n" +
                        "              \"name\": \"%s\"\n" +
                        "            }\n" +
                        "          ]\n" +
                        "        }\n" +
                        "      ]\n" +
                        "    }\n" +
                        "  ]\n" +
                        "}", gravel, dropItem);
                pack.add(new Identifier(identifier.getNamespace(), "loot_tables/blocks/" + identifier.getPath() + ".json"), new StringResource(json));
            }
        });
    }
    
    private int getMiningLevel(Block block) {
        ToolManagerImpl.Entry entry = ToolManagerImpl.entryNullable(block);
        if (entry != null) {
            return entry.getMiningLevel(FabricToolTags.PICKAXES);
        }
        if (Blocks.COAL_ORE == block) return 0;
        if (Blocks.IRON_ORE == block) return 1;
        if (Blocks.LAPIS_ORE == block) return 1;
        if (Blocks.DIAMOND_ORE == block) return 2;
        if (Blocks.EMERALD_ORE == block) return 2;
        if (Blocks.GOLD_ORE == block) return 2;
        if (Blocks.NETHER_QUARTZ_ORE == block) return 0;
        if (Blocks.REDSTONE_ORE == block) return 2;
        return 0;
    }
    
    public static FabricBlockSettings copyOfWithMaterial(Material material, MaterialColor color, Block block) {
        BlockAccessor sourceAccessor = (BlockAccessor) block;
        FabricBlockSettings settings = FabricBlockSettings.of(material, color);
        BlockSettingsAccessor settingsAccessor = (BlockSettingsAccessor) settings;
        settingsAccessor.setMaterial(material);
        settings.hardness(sourceAccessor.getHardness() * 0.7f);
        settings.resistance(sourceAccessor.getResistance() * 0.5f);
        settings.collidable(sourceAccessor.getCollidable());
        settingsAccessor.setMaterialColor(color);
        settings.sounds(sourceAccessor.getSoundGroup());
        settings.slipperiness(block.getSlipperiness());
        settings.velocityMultiplier(block.getVelocityMultiplier());
        settingsAccessor.setOpaque(sourceAccessor.getOpaque());
        return settings;
    }
}
