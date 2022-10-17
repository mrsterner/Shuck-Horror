package dev.sterner.shuckhorror.mixin.client;

import dev.sterner.shuckhorror.common.registry.SHObjects;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundSystem;
import net.minecraft.client.sound.TickableSoundInstance;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SoundSystem.class)
public abstract class SoundSystemMixin {
	@Unique
	private int muffle_cooldown = 0;

	@Shadow
	protected abstract float getSoundVolume(@Nullable SoundCategory category);

	@Inject(method = "getAdjustedVolume*", at = @At("RETURN"), cancellable = true)
	private void shuck$muffle(SoundInstance sound, CallbackInfoReturnable<Float> cir) {
		if (sound instanceof TickableSoundInstance) {
			MinecraftClient minecraftClient = MinecraftClient.getInstance();
			Entity entity = minecraftClient.cameraEntity;
			if(entity instanceof PlayerEntity player && player.world instanceof ClientWorld clientWorld){
				if(muffle_cooldown % 20 == 0){
					muffle_cooldown = 0;
					BlockPos blockPos = player.getBlockPos();
					BlockPos.Mutable mutable = new BlockPos.Mutable();
					BlockPos cornPos = null;

					for(int k = 0; k <= 3; k = k > 0 ? -k : 1 - k) {
						for(int l = 0; l < 3; ++l) {
							for(int m = 0; m <= l; m = m > 0 ? -m : 1 - m) {
								for(int n = m < l && m > -l ? l : 0; n <= l; n = n > 0 ? -n : 1 - n) {
									mutable.set(blockPos, m, k - 1, n);
									if (clientWorld.getBlockState(mutable).isOf(SHObjects.CURSED_CORN_CROP)) {
										cornPos = mutable;
									}
								}
							}
						}
					}
					if(cornPos != null){
						cir.setReturnValue(MathHelper.clamp(sound.getVolume() * this.getSoundVolume(sound.getCategory()) / 2, 0.0F, 1.0F));
					}
				}
				muffle_cooldown++;
			}

		}
	}
}
