package dev.sterner.shuckhorror.common.util;
import dev.sterner.shuckhorror.common.block.CornCropBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SHUtils {

	public static void transferBlockState(World world, BlockPos pos, BlockState newState){
		int age = world.getBlockState(pos).get(CornCropBlock.AGE);
		DoubleBlockHalf doubleBlockHalf = world.getBlockState(pos).get(CornCropBlock.HALF);
		world.setBlockState(pos, newState.with(CornCropBlock.AGE, age).with(CornCropBlock.HALF, doubleBlockHalf), 2);
	}


}
