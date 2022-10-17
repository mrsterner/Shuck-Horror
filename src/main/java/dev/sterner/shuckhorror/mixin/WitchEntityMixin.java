package dev.sterner.shuckhorror.mixin;

import dev.sterner.shuckhorror.common.entity.ai.goal.CurseCornGoal;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.WitchEntity;
import net.minecraft.entity.raid.RaiderEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WitchEntity.class)
public abstract class WitchEntityMixin extends RaiderEntity {

	protected WitchEntityMixin(EntityType<? extends RaiderEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "initGoals", at = @At("TAIL"))
	private void injectCursingGoal(CallbackInfo ci){
		this.goalSelector.add(5, new CurseCornGoal((WitchEntity)(Object) this, 1, 8));
	}


}
