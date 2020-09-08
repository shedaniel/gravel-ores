package me.shedaniel.gravelores;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.gen.decorator.DecoratorConfig;

public class SurfaceGenConfig implements DecoratorConfig {
    public static final Codec<SurfaceGenConfig> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("count").forGetter(SurfaceGenConfig::getCount),
                    Codec.FLOAT.fieldOf("possibility").forGetter(SurfaceGenConfig::getPossibility)
            ).apply(instance, SurfaceGenConfig::new));
    public final int count;
    public final float possibility;
    
    public SurfaceGenConfig(int count, float possibility) {
        this.count = count;
        this.possibility = possibility;
    }
    
    public int getCount() {
        return count;
    }
    
    public float getPossibility() {
        return possibility;
    }
}