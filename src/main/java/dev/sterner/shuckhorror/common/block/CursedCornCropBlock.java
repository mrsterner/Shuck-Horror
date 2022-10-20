package dev.sterner.shuckhorror.common.block;

import net.minecraft.block.*;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.item.FlintAndSteelItem;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class CursedCornCropBlock extends CornCropBlock{
	public static final BooleanProperty LIT = Properties.LIT;


	public CursedCornCropBlock(Settings settings) {
		super(settings);
		this.setDefaultState(this.getDefaultState().with(this.getAgeProperty(), 0).with(HALF, DoubleBlockHalf.LOWER).with(AGE, 0).with(LIT, false));
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
					double d = (double)pos.getX() + random.nextDouble();
					double e = (double)pos.getY() - 1.0 + random.nextDouble() * 3;
					double f = (double)pos.getZ() + random.nextDouble();
					world.addParticle(ParticleTypes.SOUL, d, e, f, 0.0, 0.0, 0.0);
				}
				for(int i = 0; i < random.nextInt(1) + 1; ++i) {
					double g = (double)pos.getX() + random.nextDouble();
					double h = (double)pos.getY() - 1.0 + random.nextDouble() * 3;
					double k = (double)pos.getZ() + random.nextDouble();
					world.addParticle(ParticleTypes.SOUL_FIRE_FLAME, g, h, k, 0.0, 0.0, 0.0);
				}
			}
		}
	}
}
