package dev.sterner.shuckhorror.common.entity;

import com.mojang.serialization.Dynamic;
import dev.sterner.shuckhorror.api.criteria.SHCriteria;
import dev.sterner.shuckhorror.common.entity.ai.ChildOfTheCornBrain;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

public class ChildOfTheCornEntity extends HostileEntity {
	public ChildOfTheCornEntity(EntityType<? extends HostileEntity> entityType, World world) {
		super(entityType, world);
	}

	protected void mobTick() {
		this.world.getProfiler().push("childOfTheCornBrain");
		this.getBrain().tick((ServerWorld)this.world, this);
		this.world.getProfiler().pop();
		ChildOfTheCornBrain.updateActivities(this);
		if(getTarget() instanceof PlayerEntity player){
			SHCriteria.ENTITY_TARGET_PLAYER.trigger((ServerPlayerEntity) player, this.getType());

		}
		super.mobTick();
	}

	public static DefaultAttributeContainer.Builder createChildOfTheCornAttributes() {
		return MobEntity.createMobAttributes()
				.add(EntityAttributes.GENERIC_MAX_HEALTH, 14.0)
				.add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 1.0)
				.add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2.0);
	}

	@Override
	protected Brain<?> deserializeBrain(Dynamic<?> dynamic) {
		return ChildOfTheCornBrain.create(this, dynamic);
	}
	@Override
	public Brain<ChildOfTheCornEntity> getBrain() {
		return (Brain<ChildOfTheCornEntity>) super.getBrain();
	}


}
