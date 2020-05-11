package me.shedaniel.gravelores;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;
import net.minecraft.world.gen.decorator.DecoratorConfig;

public class SurfaceGenConfig implements DecoratorConfig {
    public final int count;
    public final float possibility;
    public final float basePossibility;
    
    public SurfaceGenConfig(int count, float possibility, float basePossibility) {
        this.count = count;
        this.possibility = possibility;
        this.basePossibility = basePossibility;
    }
    
    public <T> Dynamic<T> serialize(DynamicOps<T> ops) {
        return new Dynamic<>(ops, ops.createMap(ImmutableMap.of(
                ops.createString("count"), ops.createInt(this.count),
                ops.createString("possibility"), ops.createFloat(this.possibility),
                ops.createString("base_possibility"), ops.createFloat(this.basePossibility)
        )));
    }
    
    public static SurfaceGenConfig deserialize(Dynamic<?> dynamic) {
        int count = dynamic.get("count").asInt(0);
        float possibility = dynamic.get("possibility").asFloat(0);
        float basePossibility = dynamic.get("base_possibility").asFloat(0);
        return new SurfaceGenConfig(count, possibility, basePossibility);
    }
}