package dev.sterner.shuckhorror.common.block;

import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.stat.Stats;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.event.GameEvent;

public class CornBreadBlock extends Block {
	public static final IntProperty CORN_BITES;
	protected static final VoxelShape[] BITES_TO_SHAPE;
	public static final int DEFAULT_COMPARATOR_OUTPUT;

	public CornBreadBlock(Settings settings) {
		super(settings);
		this.setDefaultState(this.getDefaultState().with(CORN_BITES, 0));
	}



	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		ItemStack itemStack = player.getStackInHand(hand);
		if (world.isClient) {
			if (tryEat(world, pos, state, player).isAccepted()) {
				return ActionResult.SUCCESS;
			}
			if (itemStack.isEmpty()) {
				return ActionResult.CONSUME;
			}
		}

		return tryEat(world, pos, state, player);
	}

	protected static ActionResult tryEat(WorldAccess world, BlockPos pos, BlockState state, PlayerEntity player) {
		if (!player.canConsume(false)) {
			return ActionResult.PASS;
		} else {
			player.incrementStat(Stats.EAT_CAKE_SLICE);
			player.getHungerManager().add(2, 0.1F);
			int i = state.get(CORN_BITES);
			world.emitGameEvent(player, GameEvent.EAT, pos);
			if (i < 6) {
				world.setBlockState(pos, state.with(CORN_BITES, i + 1), 3);
			} else {
				world.removeBlock(pos, false);
				world.emitGameEvent(player, GameEvent.BLOCK_DESTROY, pos);
			}

			return ActionResult.SUCCESS;
		}
	}

	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return BITES_TO_SHAPE[state.get(CORN_BITES)];
	}

	public static int getComparatorOutput(int bites) {
		return (4 - bites) * 2;
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(CORN_BITES);
	}

	@Override
	public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
		return getComparatorOutput(state.get(CORN_BITES));
	}



	static {
		CORN_BITES =  IntProperty.of("corn_bites", 0, 3);
		DEFAULT_COMPARATOR_OUTPUT = getComparatorOutput(0);
		BITES_TO_SHAPE = new VoxelShape[]{
				Block.createCuboidShape(1.0, 0.0, 1.0, 15.0, 8.0, 15.0),
				Block.createCuboidShape(3.0, 0.0, 1.0, 15.0, 8.0, 15.0),
				Block.createCuboidShape(5.0, 0.0, 1.0, 15.0, 8.0, 15.0),
				Block.createCuboidShape(7.0, 0.0, 1.0, 15.0, 8.0, 15.0)};
	}
}
