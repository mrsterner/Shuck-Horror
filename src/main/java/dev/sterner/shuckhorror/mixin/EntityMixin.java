package dev.sterner.shuckhorror.mixin;

import dev.sterner.shuckhorror.api.criteria.SHCriteria;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin   {


	@Inject(method = "updateKilledAdvancementCriterion", at = @At(value = "HEAD"))
	private void injectCriterion(Entity entityKilled, int score, DamageSource damageSource, CallbackInfo ci){
		if (entityKilled instanceof ServerPlayerEntity player) {
			SHCriteria.HIT_ENTITY_WITH_ITEM.trigger(player, player.getActiveItem().getItem(), (Entity)(Object)this, damageSource);
		}
	}
}
