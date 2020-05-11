package me.shedaniel.gravelores;

import net.minecraft.loot.ConstantLootTableRange;
import net.minecraft.loot.LootTableRange;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

public class OreInformation {
    public static final SurfaceGenConfig EMPTY_SURFACE_GEN_CONFIG = new SurfaceGenConfig(0, 0, 0);
    public static final Pair<Integer, Integer> EMPTY_XP = new Pair<>(0, 0);
    public static final LootTableRange EMPTY_RANGE = new ConstantLootTableRange(0);
    private final boolean dropOre;
    private boolean registerRecipes = false;
    private final Identifier drop;
    private final LootTableRange dropCount;
    private final Pair<Integer, Integer> xp;
    private final SurfaceGenConfig surfaceGen;
    private boolean cottonResource = false;
    
    public OreInformation(Identifier drop, LootTableRange dropCount, Pair<Integer, Integer> xp, SurfaceGenConfig surfaceGen) {
        this.dropOre = false;
        this.drop = drop;
        this.dropCount = dropCount;
        this.xp = xp;
        this.surfaceGen = surfaceGen;
    }
    
    public OreInformation(Identifier drop, SurfaceGenConfig surfaceGen) {
        this.dropOre = true;
        this.drop = drop;
        this.dropCount = EMPTY_RANGE;
        this.xp = EMPTY_XP;
        this.surfaceGen = surfaceGen;
    }
    
    public OreInformation(SurfaceGenConfig surfaceGen) {
        this.dropOre = true;
        this.drop = null;
        this.dropCount = EMPTY_RANGE;
        this.xp = EMPTY_XP;
        this.surfaceGen = surfaceGen;
    }
    
    public OreInformation generateRecipes() {
        this.registerRecipes = true;
        return this;
    }
    
    public boolean isRegisterRecipes() {
        return registerRecipes;
    }
    
    public void setCottonResource(boolean cottonResource) {
        this.cottonResource = cottonResource;
    }
    
    public boolean isDropOre() {
        return dropOre;
    }
    
    public Identifier getDrop() {
        return drop;
    }
    
    public LootTableRange getDropCount() {
        return dropCount;
    }
    
    public Pair<Integer, Integer> getXp() {
        return xp;
    }
    
    public SurfaceGenConfig getSurfaceGen() {
        return surfaceGen;
    }
    
    public boolean isCottonResource() {
        return cottonResource;
    }
}
