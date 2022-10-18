package dev.sterner.shuckhorror.mixin;

import dev.sterner.shuckhorror.api.event.BeeGrowCropEvent;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BeeEntity.GrowCropsGoal.class)
public abstract class GrowCropsGoalMixin extends Goal {

	@Final @Shadow BeeEntity field_20373;

	@Redirect(method = "tick()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)Z"))
	private boolean shuck$beeGrowCrop(World world, BlockPos pos, BlockState state) {
		BeeGrowCropEvent.ON_BEE_GROW_CROP.invoker().onGrow(field_20373, world.getBlockState(pos), pos);
		return world.setBlockState(pos, state);
	}
}
