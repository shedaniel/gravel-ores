package me.shedaniel.gravelores;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.world.gen.feature.FeatureConfig;

public class SurfaceGenFeatureConfig implements FeatureConfig {
    public static final Codec<SurfaceGenFeatureConfig> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    BlockState.CODEC.fieldOf("oreState").forGetter(SurfaceGenFeatureConfig::getOreState),
                    Codec.INT.fieldOf("startRadius").forGetter(SurfaceGenFeatureConfig::getStartRadius),
                    BlockState.CODEC.fieldOf("baseState").forGetter(SurfaceGenFeatureConfig::getBaseState),
                    Codec.FLOAT.fieldOf("baseStatePercentage").forGetter(SurfaceGenFeatureConfig::getBaseStatePercentage)
            ).apply(instance, SurfaceGenFeatureConfig::new));
    public final BlockState oreState;
    public final int startRadius;
    public final BlockState baseState;
    public final float baseStatePercentage;
    
    public SurfaceGenFeatureConfig(BlockState oreState, int startRadius, BlockState baseState, float baseStatePercentage) {
        this.oreState = oreState;
        this.startRadius = startRadius;
        this.baseState = baseState;
        this.baseStatePercentage = baseStatePercentage;
    }
    
    public BlockState getOreState() {
        return oreState;
    }
    
    public int getStartRadius() {
        return startRadius;
    }
    
    public BlockState getBaseState() {
        return baseState;
    }
    
    public float getBaseStatePercentage() {
        return baseStatePercentage;
    }
}
