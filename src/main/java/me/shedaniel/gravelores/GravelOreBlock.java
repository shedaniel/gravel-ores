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

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.GravelBlock;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import static net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags.SHOVELS;
import static net.minecraft.sound.BlockSoundGroup.GRAVEL;

public final class GravelOreBlock extends GravelBlock {
    private final Pair<Integer, Integer> xpLimits;
    
    public GravelOreBlock(final int miningLevel, final Pair<Integer, Integer> xpLimits) {
        super(FabricBlockSettings.copyOf(Blocks.GRAVEL).requiresTool().breakByTool(SHOVELS, miningLevel).strength(0.5F).sounds(GRAVEL));
        this.xpLimits = xpLimits;
    }
    
    @Override
    public BlockState getStateForNeighborUpdate(final BlockState state, final Direction direction, final BlockState newState, final WorldAccess world, final BlockPos pos, final BlockPos posFrom) {
        if (breaksOnAnySide(world, pos) && !world.isClient()) {
            world.breakBlock(pos, true);
            return Blocks.AIR.getDefaultState();
        } else return super.getStateForNeighborUpdate(state, direction, newState, world, pos, posFrom);
    }
    
    @Override
    public void onLanding(final World world, final BlockPos pos, final BlockState fallingBlockState, final BlockState currentStateInPos, final FallingBlockEntity fallingBlockEntity) {
        super.onLanding(world, pos, fallingBlockState, currentStateInPos, fallingBlockEntity);
        if (shouldBreak(world, pos, currentStateInPos) && !world.isClient()) world.breakBlock(pos, true);
    }
    
    @Override
    public BlockState getPlacementState(final ItemPlacementContext context) {
        if (shouldBreak(context.getWorld(), context.getBlockPos(), context.getWorld().getBlockState(context.getBlockPos()))) {
            context.getWorld().setBlockState(context.getBlockPos(), getDefaultState());
            context.getWorld().breakBlock(context.getBlockPos(), true);
            context.getStack().decrement(1);
            return Blocks.AIR.getDefaultState();
        }
        return super.getPlacementState(context);
    }
    
    @Override
    public void onStacksDropped(final BlockState state, final ServerWorld world, final BlockPos pos, final ItemStack stack) {
        super.onStacksDropped(state, world, pos, stack);
        if (EnchantmentHelper.getLevel(Enchantments.SILK_TOUCH, stack) == 0) {
            final int i = MathHelper.nextInt(world.random, xpLimits.getLeft(), xpLimits.getRight());
            if (i > 0) dropExperience(world, pos, i);
        }
    }
    
    private static boolean shouldBreak(final BlockView world, final BlockPos pos, final BlockState state) {
        return state.getFluidState().isIn(FluidTags.WATER) || breaksOnAnySide(world, pos);
    }
    
    private static boolean breaksOnAnySide(final BlockView world, final BlockPos pos) {
        final BlockPos.Mutable mutable = pos.mutableCopy();
        for (Direction direction : Direction.values()) {
            BlockState state = world.getBlockState(mutable);
            if (direction != Direction.DOWN || state.getFluidState().isIn(FluidTags.WATER)) {
                mutable.set(pos, direction);
                state = world.getBlockState(mutable);
                if (state.getFluidState().isIn(FluidTags.WATER) && state.getFluidState().getLevel() > 1 && !state.isSideSolidFullSquare(world, pos, direction.getOpposite()))
                    return true;
            }
        }
        
        return false;
    }
}
