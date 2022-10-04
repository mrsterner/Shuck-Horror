package dev.sterner.shuckhorror.common.entity;

import com.mojang.serialization.Dynamic;
import dev.sterner.shuckhorror.common.entity.ai.CornCoblinBrain;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;


public class CornCoblinEntity extends HostileEntity {
	public CornCoblinEntity(EntityType<? extends HostileEntity> entityType, World world) {
		super(entityType, world);
	}

	protected void mobTick() {
		this.world.getProfiler().push("cornCoblinBrain");
		this.getBrain().tick((ServerWorld)this.world, this);
		this.world.getProfiler().pop();
		CornCoblinBrain.updateActivities(this);
		super.mobTick();
	}

	public static DefaultAttributeContainer.Builder createCornCoblinAttributes() {
		return MobEntity.createMobAttributes()
				.add(EntityAttributes.GENERIC_MAX_HEALTH, 14.0)
				.add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 1.0)
				.add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2.0);
	}

	@Override
	protected Brain<?> deserializeBrain(Dynamic<?> dynamic) {
		return CornCoblinBrain.create(this, dynamic);
	}
	@Override
	public Brain<CornCoblinEntity> getBrain() {
		return (Brain<CornCoblinEntity>) super.getBrain();
	}
}
