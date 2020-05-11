package me.shedaniel.gravelores;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.world.gen.feature.FeatureConfig;

public class SurfaceGenFeatureConfig implements FeatureConfig {
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
    
    public <T> Dynamic<T> serialize(DynamicOps<T> ops) {
        return new Dynamic<>(ops, ops.createMap(ImmutableMap.of(ops.createString("state"), BlockState.serialize(ops, this.oreState).getValue(),
                ops.createString("start_radius"), ops.createInt(this.startRadius),
                ops.createString("base_state"), BlockState.serialize(ops, this.baseState).getValue(),
                ops.createString("base_state_percentage"), ops.createFloat(this.baseStatePercentage))));
    }
    
    public static <T> SurfaceGenFeatureConfig deserialize(Dynamic<T> dynamic) {
        BlockState blockState = dynamic.get("state").map(BlockState::deserialize).orElse(Blocks.AIR.getDefaultState());
        int startRadius = dynamic.get("start_radius").asInt(0);
        BlockState baseState = dynamic.get("base_state").map(BlockState::deserialize).orElse(Blocks.AIR.getDefaultState());
        float baseStatePercentage = dynamic.get("base_state_percentage").asFloat(0);
        return new SurfaceGenFeatureConfig(blockState, startRadius, baseState, baseStatePercentage);
    }
}
