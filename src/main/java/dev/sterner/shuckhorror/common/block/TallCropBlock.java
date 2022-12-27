package dev.sterner.shuckhorror.common.block;

import dev.sterner.shuckhorror.common.registry.SHObjects;
import dev.sterner.shuckhorror.common.util.Constants;
import dev.sterner.shuckhorror.common.util.SHUtils;
import net.minecraft.block.*;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

public class TallCropBlock extends CropBlock {
	public static final IntProperty AGE;
	public static final int MAX_AGE = 5;
	public static final int UPPER_START_AGE = 3;
	public static final EnumProperty<DoubleBlockHalf> HALF;
	private static final VoxelShape FULL_BOTTOM;
	private static final VoxelShape[] LOWER_SHAPES;

	public TallCropBlock(Settings settings) {
		super(settings);
		this.setDefaultState(this.getDefaultState().with(this.getAgeProperty(), 0).with(HALF, DoubleBlockHalf.LOWER).with(AGE, 0));
	}

	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
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
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if(SHUtils.harvestCorn(player, world, state, pos)){
			return ActionResult.CONSUME;
		}
		return super.onUse(state, world, pos, player, hand, hit);
	}

	@Override
	public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		if (!world.isClient) {
			if (player.isCreative()) {
				onBreakInCreative(world, pos, state, player);
			} else {
				dropStacks(state, world, pos, null, player, player.getMainHandStack());
			}
		}

		super.onBreak(world, pos, state, player);
	}

	public static void onBreakInCreative(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		DoubleBlockHalf doubleBlockHalf = (DoubleBlockHalf)state.get(HALF);
		if (doubleBlockHalf == DoubleBlockHalf.UPPER) {
			BlockPos blockPos = pos.down();
			BlockState blockState = world.getBlockState(blockPos);
			if (blockState.isOf(state.getBlock()) && blockState.get(HALF) == DoubleBlockHalf.LOWER) {
				BlockState blockState2 = blockState.contains(Properties.WATERLOGGED) && (Boolean)blockState.get(Properties.WATERLOGGED) ? Blocks.WATER.getDefaultState() : Blocks.AIR.getDefaultState();
				world.setBlockState(blockPos, blockState2, 35);
				world.syncWorldEvent(player, 2001, blockPos, Block.getRawIdFromState(blockState));
			}
		}

	}

	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		if (state.get(HALF) == DoubleBlockHalf.LOWER) {
			BlockState upperBlockState = world.getBlockState(pos.up());
			if (upperBlockState.isIn(Constants.Tags.CORN) && this.getAge(upperBlockState) < UPPER_START_AGE){
				return false;
			}
			return super.canPlaceAt(state, world, pos);
		} else {
			if (this.getAge(state) < UPPER_START_AGE){
				return false;
			}
			BlockState blockstate = world.getBlockState(pos.down());
			if (!blockstate.isIn(Constants.Tags.CORN)){
				return super.canPlaceAt(state, world, pos);
			}
			return blockstate.isIn(Constants.Tags.CORN) && blockstate.get(HALF) == DoubleBlockHalf.LOWER && this.getAge(state) == this.getAge(blockstate);
		}
	}


	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		DoubleBlockHalf doubleBlockHalf = state.get(HALF);
		if (direction.getAxis() == Direction.Axis.Y && doubleBlockHalf == DoubleBlockHalf.LOWER == (direction == Direction.UP) && (!neighborState.isIn(Constants.Tags.CORN) || neighborState.get(HALF) == doubleBlockHalf)) {
			return Blocks.AIR.getDefaultState();
		} else {
			return doubleBlockHalf == DoubleBlockHalf.LOWER && direction == Direction.DOWN && !state.canPlaceAt(world, pos) ? Blocks.AIR.getDefaultState() : super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
		}
	}

	@Override
	protected int getGrowthAmount(World world) {
		return MathHelper.nextInt(world.random, 1, 3);
	}

	@Override
	public void applyGrowth(World world, BlockPos pos, BlockState state) {
		if (state.get(HALF) == DoubleBlockHalf.UPPER) {
			pos = pos.down();
		}

		int newAgeTriedToDestroyTheMetal = this.getAge(state) + getGrowthAmount(world);
		newAgeTriedToDestroyTheMetal = Math.min(newAgeTriedToDestroyTheMetal, this.getMaxAge());

		if (newAgeTriedToDestroyTheMetal >= UPPER_START_AGE) {
			if (!this.canGrow(world, world.getRandom(), pos, state)){
				return;
			}
			world.setBlockState(pos.up(), withAge(newAgeTriedToDestroyTheMetal).with(HALF, DoubleBlockHalf.UPPER), 3);
		}
		world.setBlockState(pos, withAge(newAgeTriedToDestroyTheMetal), 2);
	}

	@Override
	public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
		BlockState blockState = world.getBlockState(pos.up());
		return blockState.getBlock() instanceof TallCropBlock || blockState.getMaterial().isReplaceable();
	}

	public boolean canGrowUp(BlockView world, BlockPos downPos) {
		BlockState state = world.getBlockState(downPos.up());
		return state.getBlock() instanceof TallCropBlock || state.getMaterial().isReplaceable();
	}

	@Override
	public boolean isFertilizable(BlockView world, BlockPos pos, BlockState state, boolean isClient) {
		return state.get(HALF) == DoubleBlockHalf.LOWER && (!this.isMature(state) && (this.canGrowUp(world, pos) || this.getAge(state) < UPPER_START_AGE - 1));

	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		if (state.get(HALF) == DoubleBlockHalf.LOWER) {
			return LOWER_SHAPES[state.get(AGE)];
		}
		return Block.createCuboidShape(0, 0, 0, 16, 16, 16);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(HALF).add(AGE);
	}

	@Override
	public IntProperty getAgeProperty() {
		return AGE;
	}

	@Override
	protected ItemConvertible getSeedsItem() {
		return SHObjects.CORN_KERNELS;
	}

	@Override
	public int getMaxAge() {
		return MAX_AGE;
	}

	static {
		AGE = Properties.AGE_5;
		HALF = EnumProperty.of("half", DoubleBlockHalf.class);
		FULL_BOTTOM = Block.createCuboidShape(0, -1, 0, 16, 16, 16);
		LOWER_SHAPES = new VoxelShape[]{
				Block.createCuboidShape(0, -1, 0, 16, 5, 16),
				Block.createCuboidShape(0, -1, 0, 16, 10, 16),
				Block.createCuboidShape(0, -1, 0, 16, 16, 16),
				Block.createCuboidShape(0, -1, 0, 16, 16, 16),
				Block.createCuboidShape(0, -1, 0, 16, 16, 16),
				FULL_BOTTOM,
				FULL_BOTTOM,
				FULL_BOTTOM
		};
	}
}
