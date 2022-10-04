package dev.sterner.shuckhorror.common.block;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

public class CornCropBlock extends CropBlock {
	public static final IntProperty AGE;
	public static final int MAX_AGE = 5;
	public static final int UPPER_START_AGE = 4;
	public static final EnumProperty<DoubleBlockHalf> HALF;
	private static final VoxelShape FULL_BOTTOM;
	private static final VoxelShape[] LOWER_SHAPES;
	private static final VoxelShape[] UPPER_SHAPES;


	public CornCropBlock(Settings settings) {
		super(settings);
		this.setDefaultState(this.stateManager.getDefaultState().with(this.getAgeProperty(), 0).with(HALF, DoubleBlockHalf.LOWER).with(AGE, 0));
	}

	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, RandomGenerator random) {
		if(state.get(HALF) == DoubleBlockHalf.LOWER){
			if (world.getBaseLightLevel(pos, 0) >= 9) {
				int i = this.getAge(state);
				if (i < this.getMaxAge()) {
					float f = getAvailableMoisture(this, world, pos);
					if (random.nextInt((int)(25.0F / f) + 1) == 0) {
						if(i + 1 >= UPPER_START_AGE){
							world.setBlockState(pos.up(), this.withAge(i + 1).with(HALF, DoubleBlockHalf.UPPER), 4);
						}
						world.setBlockState(pos, this.withAge(i + 1), 2);
					}
				}
			}
		}
	}

	@Override
	public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		if (!world.isClient) {
			if (player.isCreative()) {
				TallPlantBlock.onBreakInCreative(world, pos, state, player);
			} else {
				dropStacks(state, world, pos, (BlockEntity)null, player, player.getMainHandStack());
			}
		}

		super.onBreak(world, pos, state, player);
	}

	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		if (state.get(HALF) == DoubleBlockHalf.LOWER) {
			BlockState upperBlockState = world.getBlockState(pos.up());
			if (upperBlockState.getBlock() == this && this.getAge(upperBlockState) < UPPER_START_AGE){
				return false;
			}
			return super.canPlaceAt(state, world, pos);
		} else {
			if (this.getAge(state) < UPPER_START_AGE){
				return false;
			}
			BlockState blockstate = world.getBlockState(pos.down());
			if (state.getBlock() != this){
				return super.canPlaceAt(state, world, pos);
			}
			return blockstate.isOf(this) && blockstate.get(HALF) == DoubleBlockHalf.LOWER && this.getAge(state) == this.getAge(blockstate);
		}
	}


	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		DoubleBlockHalf doubleBlockHalf = (DoubleBlockHalf)state.get(HALF);
		if (direction.getAxis() == Direction.Axis.Y && doubleBlockHalf == DoubleBlockHalf.LOWER == (direction == Direction.UP) && (!neighborState.isOf(this) || neighborState.get(HALF) == doubleBlockHalf)) {
			return Blocks.AIR.getDefaultState();
		} else {
			return doubleBlockHalf == DoubleBlockHalf.LOWER && direction == Direction.DOWN && !state.canPlaceAt(world, pos) ? Blocks.AIR.getDefaultState() : super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
		}
	}

	@Override
	public void applyGrowth(World world, BlockPos pos, BlockState state) {
		int newAgeTriedToDestroyTheMetal = this.getAge(state) + getGrowthAmount(world);
		newAgeTriedToDestroyTheMetal = Math.min(newAgeTriedToDestroyTheMetal, this.getMaxAge());
		if (state.get(HALF) == DoubleBlockHalf.UPPER) {
			pos = pos.down();
		}
		if (newAgeTriedToDestroyTheMetal >= UPPER_START_AGE) {
			if (!this.canGrow(world, world.getRandom(), pos, state)) return;
			world.setBlockState(pos.up(), withAge(newAgeTriedToDestroyTheMetal).with(HALF, DoubleBlockHalf.UPPER), 3);
		}
		world.setBlockState(pos, withAge(newAgeTriedToDestroyTheMetal), 2);
	}

	@Override
	public boolean canGrow(World world, RandomGenerator random, BlockPos pos, BlockState state) {
		BlockState blockState = world.getBlockState(pos.up());
		return blockState.getBlock() instanceof CornCropBlock || blockState.getMaterial().isReplaceable();
	}

	@Override
	public boolean isFertilizable(BlockView world, BlockPos pos, BlockState state, boolean isClient) {
		return state.get(HALF) == DoubleBlockHalf.LOWER && (state.get(AGE) != getMaxAge() && this.getAge(state) < UPPER_START_AGE - 1);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		if (state.get(HALF) == DoubleBlockHalf.LOWER) {
			return LOWER_SHAPES[state.get(AGE)];
		}
		return UPPER_SHAPES[state.get(AGE)];
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(HALF).add(AGE);
	}

	@Override
	protected ItemConvertible getSeedsItem() {
		return super.getSeedsItem();//TODO
	}

	@Override
	public int getMaxAge() {
		return MAX_AGE;
	}

	static {
		AGE = Properties.AGE_5;
		HALF = Properties.DOUBLE_BLOCK_HALF;
		FULL_BOTTOM = Block.createCuboidShape(1, 0, 1, 15, 16, 15);
		LOWER_SHAPES = new VoxelShape[]{
				Block.createCuboidShape(1, 0, 1, 15, 5, 15),
				Block.createCuboidShape(1, 0, 1, 15, 10, 15),
				Block.createCuboidShape(1, 0, 1, 15, 15, 15),
				Block.createCuboidShape(1, 0, 1, 15, 16, 15),
				Block.createCuboidShape(1, 0, 1, 15, 16, 15),
				FULL_BOTTOM,
				FULL_BOTTOM,
				FULL_BOTTOM
		};
		UPPER_SHAPES = new VoxelShape[]{
				FULL_BOTTOM,
				FULL_BOTTOM,
				FULL_BOTTOM,
				FULL_BOTTOM,
				Block.createCuboidShape(1, 0, 1, 15, 3, 15),
				Block.createCuboidShape(1, 0, 1, 15, 7, 15),
				Block.createCuboidShape(1, 0, 1, 15, 10, 15),
				Block.createCuboidShape(1, 0, 1, 15, 16, 15)
		};
	}
}
