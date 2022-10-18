package dev.sterner.shuckhorror.mixin;

import dev.sterner.shuckhorror.api.event.BeeGrowCropEvent;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.sound.SoundEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BeeEntity.PollinateGoal.class)
public class PollinateGoalMixin {


	@Redirect(method = "tick()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/passive/BeeEntity;playSound(Lnet/minecraft/sound/SoundEvent;FF)V"))
	private void shuck$beeGrowCrop(BeeEntity instance, SoundEvent soundEvent, float v, float u) {
		BeeGrowCropEvent.ON_BEE_POLLINATE.invoker().onPollinate(instance, instance.world.getBlockState(instance.getBlockPos().down()), instance.getBlockPos().down());
	}
}
