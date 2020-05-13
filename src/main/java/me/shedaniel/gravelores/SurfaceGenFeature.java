package me.shedaniel.gravelores;

import com.mojang.datafixers.Dynamic;
import net.minecraft.block.BlockState;
import net.minecraft.class_5281;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;

import java.util.Random;
import java.util.function.Function;

public class SurfaceGenFeature extends Feature<SurfaceGenFeatureConfig> {
    public SurfaceGenFeature(Function<Dynamic<?>, ? extends SurfaceGenFeatureConfig> configFactory) {
        super(configFactory);
    }
    
    @Override
    public boolean generate(class_5281 world, StructureAccessor accessor, ChunkGenerator generator, Random random, BlockPos pos, SurfaceGenFeatureConfig config) {
        if (world.getWorld().getDimension().getType() != DimensionType.OVERWORLD) return false;
        while (true) {
            label48:
            {
                if (pos.getY() > 3) {
                    if (world.isAir(pos.down())) {
                        break label48;
                    }
                    
                    BlockState block = world.getBlockState(pos.down());
                    if (!isDirt(block.getBlock()) && !isStone(block.getBlock()) && !block.isOpaque()) {
                        break label48;
                    }
                }
                
                if (pos.getY() <= 3) {
                    return false;
                }
                
                int i = config.startRadius;
                
                for (int j = 0; i >= 0 && j < 3; ++j) {
                    int k = i + random.nextInt(2);
                    int l = i + random.nextInt(3);
                    int m = i + random.nextInt(2);
                    float f = (float) (k + l + m) * 0.333F + 0.5F;
                    
                    for (BlockPos blockPos2 : BlockPos.iterate(pos.add(-k, -l, -m), pos.add(k, l, m))) {
                        if (blockPos2.getSquaredDistance(pos) <= (double) (f * f)) {
                            boolean isBase = random.nextFloat() <= config.baseStatePercentage;
                            world.setBlockState(blockPos2, isBase ? config.baseState : config.oreState, 4);
                        }
                    }
                    
                    pos = pos.add(-(i + 1) + random.nextInt(2 + i * 2), -random.nextInt(2), -(i + 1) + random.nextInt(2 + i * 2));
                }
                
                return true;
            }
            
            pos = pos.down();
        }
    }
}
