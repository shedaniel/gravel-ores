package me.shedaniel.gravelores;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.GravelBlock;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.Random;

public class GravelOreBlock extends GravelBlock {
    private final Pair<Integer, Integer> xp;
    
    public GravelOreBlock(Block.Settings settings, Pair<Integer, Integer> xp) {
        super(settings);
        this.xp = xp;
    }
    
    protected int getExperienceWhenMined(Random random) {
        return MathHelper.nextInt(random, xp.getLeft(), xp.getRight());
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public void onStacksDropped(BlockState state, World world, BlockPos pos, ItemStack stack) {
        super.onStacksDropped(state, world, pos, stack);
        if (EnchantmentHelper.getLevel(Enchantments.SILK_TOUCH, stack) == 0) {
            int i = this.getExperienceWhenMined(world.random);
            if (i > 0) {
                this.dropExperience(world, pos, i);
            }
        }
        
    }
}
