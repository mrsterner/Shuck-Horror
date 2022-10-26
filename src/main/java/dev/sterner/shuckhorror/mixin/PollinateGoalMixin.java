package dev.sterner.shuckhorror.mixin;

import dev.sterner.shuckhorror.common.registry.SHObjects;
import dev.sterner.shuckhorror.common.util.SHUtils;
import net.minecraft.entity.passive.BeeEntity;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BeeEntity.PollinateGoal.class)
public class PollinateGoalMixin {

	@Dynamic
	private BeeEntity beeEntity;

	@Inject(method = "<init>", at = @At(value = "TAIL"))
	private void shuck$init(BeeEntity bee, CallbackInfo ci) {
		beeEntity = bee;
	}

	@Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/passive/BeeEntity;playSound(Lnet/minecraft/sound/SoundEvent;FF)V"))
	private void shuck$tick(CallbackInfo ci){
		if (beeEntity.getFlowerPos() != null && beeEntity.world.getBlockState(beeEntity.getFlowerPos()).isOf(SHObjects.CURSED_CORN_CROP)) {
			SHUtils.transferBlockState(beeEntity.world, beeEntity.getFlowerPos(), SHObjects.CANDY_CORN_CROP.getDefaultState());
			if (beeEntity.world.getBlockState(beeEntity.getFlowerPos().up()).isOf(SHObjects.CURSED_CORN_CROP)) {
				SHUtils.transferBlockState(beeEntity.world, beeEntity.getFlowerPos().up(), SHObjects.CANDY_CORN_CROP.getDefaultState());
			}
			if (beeEntity.world.getBlockState(beeEntity.getFlowerPos().down()).isOf(SHObjects.CURSED_CORN_CROP)) {
				SHUtils.transferBlockState(beeEntity.world, beeEntity.getFlowerPos().down(), SHObjects.CANDY_CORN_CROP.getDefaultState());
			}
		}
	}
}
