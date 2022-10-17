package dev.sterner.shuckhorror.common.entity.ai.task;

import com.google.common.collect.ImmutableMap;
import dev.sterner.shuckhorror.common.entity.CornCoblinEntity;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;

import java.util.Map;

public class SHEmergeTask <E extends CornCoblinEntity> extends Task<E> {
	public SHEmergeTask(int duration) {
		super(ImmutableMap.of(
						MemoryModuleType.IS_EMERGING,
						MemoryModuleState.VALUE_PRESENT,
						MemoryModuleType.WALK_TARGET,
						MemoryModuleState.VALUE_ABSENT,
						MemoryModuleType.LOOK_TARGET,
						MemoryModuleState.REGISTERED
				),
				duration
		);
	}

	protected boolean shouldKeepRunning(ServerWorld serverWorld, E wardenEntity, long l) {
		return true;
	}

	protected void run(ServerWorld serverWorld, E entity, long l) {
		entity.setPose(EntityPose.EMERGING);
	}

	protected void finishRunning(ServerWorld serverWorld, E wardenEntity, long l) {
		if (wardenEntity.isInPose(EntityPose.EMERGING)) {
			wardenEntity.setPose(EntityPose.STANDING);
		}

	}
}
