package dev.sterner.shuckhorror.common.util;
import dev.sterner.shuckhorror.common.block.CornCropBlock;
import dev.sterner.shuckhorror.common.block.TallCropBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class SHUtils {

	public static void transferBlockState(World world, BlockPos pos, BlockState newState){
		if(world.getBlockState(pos).isIn(Constants.Tags.CORN)){
			int age = world.getBlockState(pos).get(CornCropBlock.AGE);
			DoubleBlockHalf doubleBlockHalf = world.getBlockState(pos).get(CornCropBlock.HALF);
			world.setBlockState(pos, newState.with(CornCropBlock.AGE, age).with(CornCropBlock.HALF, doubleBlockHalf), 2);
		}
	}

	public static boolean harvestCorn(PlayerEntity playerEntity, World world, BlockState state, BlockPos pos){
		if(state.get(TallCropBlock.HALF) == DoubleBlockHalf.UPPER && state.get(TallCropBlock.AGE) == TallCropBlock.MAX_AGE){
			world.playSound(null, pos, SoundEvents.BLOCK_CROP_BREAK, SoundCategory.BLOCKS, 1.0F, 0.8F + world.random.nextFloat() * 0.4F);
			BlockState blockState = state.with(TallCropBlock.AGE, 3).with(TallCropBlock.HALF, DoubleBlockHalf.UPPER);
			BlockState blockState2 = state.with(TallCropBlock.AGE, 3).with(TallCropBlock.HALF, DoubleBlockHalf.LOWER);
			world.setBlockState(pos, blockState, Block.NOTIFY_LISTENERS);
			if(world.getBlockState(pos.down()).isIn(Constants.Tags.CORN)){
				world.setBlockState(pos.down(), blockState2, Block.NOTIFY_LISTENERS);
			}
			world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(playerEntity, blockState));
			Block.dropStacks(state, world, pos, null, playerEntity, playerEntity.getMainHandStack());
			return true;
		}
		return false;
	}
}
