package me.shedaniel.gravelores;

import com.mojang.datafixers.Dynamic;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorConfig;
import net.minecraft.world.gen.decorator.Decorator;

import java.util.Random;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class SurfaceGenDecorator extends Decorator<SurfaceGenConfig> {
    public SurfaceGenDecorator(Function<Dynamic<?>, ? extends SurfaceGenConfig> function) {
        super(function);
    }
    
    public Stream<BlockPos> getPositions(IWorld world, ChunkGenerator<? extends ChunkGeneratorConfig> chunkGenerator, Random random, SurfaceGenConfig config, BlockPos blockPos) {
        int i = random.nextInt(config.count);
        int count = 0;
        for (int j = 0; j < i; j++) {
            boolean generate = random.nextFloat() <= config.possibility;
            if (generate) {
                count++;
            }
        }
        return IntStream.range(0, count).mapToObj((ix) -> {
            int j = random.nextInt(16) + blockPos.getX();
            int k = random.nextInt(16) + blockPos.getZ();
            int l = world.getTopY(Heightmap.Type.MOTION_BLOCKING, j, k);
            return new BlockPos(j, l, k);
        });
    }
}