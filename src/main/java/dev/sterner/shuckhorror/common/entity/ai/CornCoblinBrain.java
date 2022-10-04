package dev.sterner.shuckhorror.common.entity.ai;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Dynamic;
import dev.sterner.shuckhorror.common.entity.CornCoblinEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Activity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.ai.brain.sensor.SensorType;

import java.util.List;

public class CornCoblinBrain {
	private static final List<SensorType<? extends Sensor<? super CornCoblinEntity>>> SENSORS = List.of(
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

	public CornCoblinBrain(){}

	public static Brain<?> create(CornCoblinEntity cornCoblinEntity, Dynamic<?> dynamic) {
		Brain.Profile<CornCoblinEntity> profile = Brain.createProfile(MEMORIES, SENSORS);
		Brain<CornCoblinEntity> brain = profile.deserialize(dynamic);
		addCoreActivities(brain);
		addIdleActivities(brain);
		addFightActivities(cornCoblinEntity, brain);
		brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
		brain.setDefaultActivity(Activity.IDLE);
		brain.resetPossibleActivities();
		return brain;
	}

	private static void addCoreActivities(Brain<CornCoblinEntity> brain) {

	}

	private static void addIdleActivities(Brain<CornCoblinEntity> brain) {

	}

	private static void addFightActivities(CornCoblinEntity cornCoblinEntity, Brain<CornCoblinEntity> brain) {

	}

	public static void updateActivities(CornCoblinEntity cornCoblinEntity) {
		cornCoblinEntity.getBrain().resetPossibleActivities(ImmutableList.of(Activity.FIGHT, Activity.IDLE));
		cornCoblinEntity.setAttacking(cornCoblinEntity.getBrain().hasMemoryModule(MemoryModuleType.ATTACK_TARGET));
	}

	private static boolean isTarget(CornCoblinEntity leshonEntity, LivingEntity entity) {
		return leshonEntity.getBrain().getOptionalMemory(MemoryModuleType.ATTACK_TARGET).filter(targetedEntity -> targetedEntity == entity).isPresent();
	}
}
