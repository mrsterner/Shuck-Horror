package dev.sterner.shuckhorror.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class CursedCornCropBlock extends CornCropBlock{
	public static final BooleanProperty LIT = Properties.LIT;


	public CursedCornCropBlock(Settings settings) {
		super(settings);
	}

	@Override
	protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
		return floor.isOf(Blocks.FARMLAND) || floor.isIn(BlockTags.DIRT);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(LIT);
		super.appendProperties(builder);
	}

	@Override
	public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
		return true;
	}

	@Override
	public boolean canMobSpawnInside() {
		return true;
	}



	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		super.randomDisplayTick(state, world, pos, random);
		if(state.get(LIT)){
			if (random.nextInt(5) == 0) {
				for(int i = 0; i < random.nextInt(1) + 1; ++i) {
					world.addParticle(
							ParticleTypes.SOUL_FIRE_FLAME,
							(double)pos.getX() + 0.5,
							(double)pos.getY() + 0.5,
							(double)pos.getZ() + 0.5,
							(double)(random.nextFloat() / 2.0F),
							5.0E-5,
							(double)(random.nextFloat() / 2.0F)
					);
				}
			}
		}
	}
}
