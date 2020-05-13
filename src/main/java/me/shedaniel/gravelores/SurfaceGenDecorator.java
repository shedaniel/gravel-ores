package me.shedaniel.gravelores;

import com.mojang.datafixers.Dynamic;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.decorator.Decorator;

import java.util.Random;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class SurfaceGenDecorator extends Decorator<SurfaceGenConfig> {
    public SurfaceGenDecorator(Function<Dynamic<?>, ? extends SurfaceGenConfig> function) {
        super(function);
    }
    
    @Override
    public Stream<BlockPos> getPositions(IWorld world, ChunkGenerator generator, Random random, SurfaceGenConfig config, BlockPos pos) {
        int i = random.nextInt(config.count);
        int count = 0;
        for (int j = 0; j < i; j++) {
            boolean generate = random.nextFloat() <= config.possibility;
            if (generate) {
                count++;
            }
        }
        return IntStream.range(0, count).mapToObj((ix) -> {
            int j = random.nextInt(16) + pos.getX();
            int k = random.nextInt(16) + pos.getZ();
            int l = world.getTopY(Heightmap.Type.MOTION_BLOCKING, j, k);
            return new BlockPos(j, l, k);
        });
    }
}