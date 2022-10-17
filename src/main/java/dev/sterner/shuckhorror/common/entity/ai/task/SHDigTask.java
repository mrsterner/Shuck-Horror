package dev.sterner.shuckhorror.common.entity.ai.task;

import com.google.common.collect.ImmutableMap;
import dev.sterner.shuckhorror.common.entity.CornCoblinEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.server.world.ServerWorld;

public class SHDigTask<E extends CornCoblinEntity> extends Task<E> {
	public SHDigTask(int duration) {
		super(ImmutableMap.of(
				MemoryModuleType.ATTACK_TARGET,
				MemoryModuleState.VALUE_ABSENT,
				MemoryModuleType.WALK_TARGET,
				MemoryModuleState.VALUE_ABSENT),
				duration);
	}

	protected boolean shouldKeepRunning(ServerWorld serverWorld, E entity, long l) {
		return entity.getRemovalReason() == null;
	}

	protected boolean shouldRun(ServerWorld serverWorld, E entity) {
		return entity.isOnGround() || entity.isTouchingWater() || entity.isInLava();
	}

	protected void run(ServerWorld serverWorld, E entity, long l) {
		if (entity.isOnGround()) {
			entity.setPose(EntityPose.DIGGING);
		} else {
			this.finishRunning(serverWorld, entity, l);
		}

	}

	protected void finishRunning(ServerWorld serverWorld, E entity, long l) {
		if (entity.getRemovalReason() == null) {
			entity.remove(Entity.RemovalReason.DISCARDED);
		}

	}
}
