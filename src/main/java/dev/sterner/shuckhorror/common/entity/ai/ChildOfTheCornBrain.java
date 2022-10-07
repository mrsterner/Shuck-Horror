package dev.sterner.shuckhorror.common.entity.ai;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Dynamic;
import dev.sterner.shuckhorror.common.entity.ChildOfTheCornEntity;
import dev.sterner.shuckhorror.common.entity.CornCoblinEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Activity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.ai.brain.sensor.SensorType;

import java.util.List;

public class ChildOfTheCornBrain {
	private static final List<SensorType<? extends Sensor<? super ChildOfTheCornEntity>>> SENSORS = List.of(
			SensorType.NEAREST_PLAYERS,
			SensorType.NEAREST_LIVING_ENTITIES,
			SensorType.HURT_BY);
	private static final List<MemoryModuleType<?>> MEMORIES = List.of(
			MemoryModuleType.MOBS,
			MemoryModuleType.VISIBLE_MOBS,
			MemoryModuleType.NEAREST_VISIBLE_PLAYER,
			MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER,
			MemoryModuleType.LOOK_TARGET,
			MemoryModuleType.WALK_TARGET,
			MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE,
			MemoryModuleType.PATH,
			MemoryModuleType.ANGRY_AT,
			MemoryModuleType.ATTACK_TARGET,
			MemoryModuleType.ATTACK_COOLING_DOWN,
			MemoryModuleType.NEAREST_ATTACKABLE,
			MemoryModuleType.HOME,
			MemoryModuleType.PACIFIED,
			MemoryModuleType.NEAREST_REPELLENT,
			MemoryModuleType.AVOID_TARGET
	);

	public ChildOfTheCornBrain(){}

	public static Brain<?> create(ChildOfTheCornEntity childOfTheCornEntity, Dynamic<?> dynamic) {
		Brain.Profile<ChildOfTheCornEntity> profile = Brain.createProfile(MEMORIES, SENSORS);
		Brain<ChildOfTheCornEntity> brain = profile.deserialize(dynamic);
		addCoreActivities(brain);
		addIdleActivities(brain);
		addFightActivities(childOfTheCornEntity, brain);
		brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
		brain.setDefaultActivity(Activity.IDLE);
		brain.resetPossibleActivities();
		return brain;
	}

	private static void addCoreActivities(Brain<ChildOfTheCornEntity> brain) {

	}

	private static void addIdleActivities(Brain<ChildOfTheCornEntity> brain) {

	}

	private static void addFightActivities(ChildOfTheCornEntity cornCoblinEntity, Brain<ChildOfTheCornEntity> brain) {

	}

	public static void updateActivities(ChildOfTheCornEntity childOfTheCornEntity) {
		childOfTheCornEntity.getBrain().resetPossibleActivities(ImmutableList.of(Activity.FIGHT, Activity.IDLE));
		childOfTheCornEntity.setAttacking(childOfTheCornEntity.getBrain().hasMemoryModule(MemoryModuleType.ATTACK_TARGET));
	}

	private static boolean isTarget(ChildOfTheCornEntity childOfTheCornEntity, LivingEntity entity) {
		return childOfTheCornEntity.getBrain().getOptionalMemory(MemoryModuleType.ATTACK_TARGET).filter(targetedEntity -> targetedEntity == entity).isPresent();
	}
}
