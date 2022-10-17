package dev.sterner.shuckhorror.common.util;
import dev.sterner.shuckhorror.common.block.CornCropBlock;
import dev.sterner.shuckhorror.common.registry.SHObjects;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SHUtils {


	public static void transferBlockState(World world, BlockPos pos){
		int age = world.getBlockState(pos).get(CornCropBlock.AGE);
		DoubleBlockHalf doubleBlockHalf = world.getBlockState(pos).get(CornCropBlock.HALF);
		world.setBlockState(pos, SHObjects.CURSED_CORN_CROP.getDefaultState().with(CornCropBlock.AGE, age).with(CornCropBlock.HALF, doubleBlockHalf), 2);
	}
}
