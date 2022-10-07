package dev.sterner.shuckhorror.common.entity.ai;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import dev.sterner.shuckhorror.common.entity.CornCoblinEntity;
import dev.sterner.shuckhorror.common.entity.ai.task.SHDigTask;
import dev.sterner.shuckhorror.common.entity.ai.task.SHEmergeTask;
import dev.sterner.shuckhorror.common.registry.SHBrains;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.*;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraft.entity.ai.brain.task.*;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.warden.ForceUnmountTask;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Unit;
import net.minecraft.util.math.MathHelper;

import java.util.List;
import java.util.Optional;

public class CornCoblinBrain {
	private static final int DIG_DURATION = MathHelper.ceil(100.0F);
	public static final int EMERGE_DURATION = MathHelper.ceil(133.59999F);

	private static final List<SensorType<? extends Sensor<? super CornCoblinEntity>>> SENSORS = List.of(
			SensorType.NEAREST_PLAYERS,
			SensorType.NEAREST_LIVING_ENTITIES,
			SensorType.HURT_BY,
			SHBrains.CORN_COBLIN_SPECIFIC_SENSOR);
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
			MemoryModuleType.AVOID_TARGET,
			MemoryModuleType.IS_EMERGING,
			MemoryModuleType.DIG_COOLDOWN,
			SHBrains.TARGET_BLOCKS
	);

	private static final Task<CornCoblinEntity> DIG_COOLDOWN_TASK = new Task<>(ImmutableMap.of(MemoryModuleType.DIG_COOLDOWN, MemoryModuleState.REGISTERED)) {
		protected void run(ServerWorld serverWorld, CornCoblinEntity cornCoblinEntity, long l) {
			CornCoblinBrain.resetDigCooldown(cornCoblinEntity);
		}
	};

	public CornCoblinBrain(){}

	public static Brain<?> create(CornCoblinEntity cornCoblinEntity, Dynamic<?> dynamic) {
		Brain.Profile<CornCoblinEntity> profile = Brain.createProfile(MEMORIES, SENSORS);
		Brain<CornCoblinEntity> brain = profile.deserialize(dynamic);
		addCoreActivities(brain);
		addIdleActivities(brain);
		addEmergeActivities(brain);
		addDigActivities(brain);
		addFightActivities(cornCoblinEntity, brain);
		brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
		brain.setDefaultActivity(Activity.IDLE);
		brain.resetPossibleActivities();
		return brain;
	}

	private static void addCoreActivities(Brain<CornCoblinEntity> brain) {
		brain.setTaskList(
				Activity.CORE,
				0,
				ImmutableList.of(
						new StayAboveWaterTask(0.6f),
						new LookAroundTask(45, 90),
						new WanderAroundTask(),
						new UpdateAttackTargetTask<>(CornCoblinBrain::getAttackTarget)
				)
		);
	}

	private static void addIdleActivities(Brain<CornCoblinEntity> brain) {
		brain.setTaskList(
				Activity.IDLE,
				ImmutableList.of(
						Pair.of(0, new RandomTask<>(
								ImmutableList.of(
										Pair.of(new StrollTask(0.6F), 2),
										Pair.of(new ConditionalTask<>(livingEntity -> true, new GoTowardsLookTarget(0.6F, 3)), 2),
										Pair.of(new WaitTask(30, 60), 1)
								)))
				)
		);
	}

	private static void addFightActivities(CornCoblinEntity cornCoblinEntity, Brain<CornCoblinEntity> brain) {
		brain.setTaskList(
				Activity.FIGHT,
				10,
				ImmutableList.of(
						DIG_COOLDOWN_TASK,
						new ForgetAttackTargetTask<>(entity -> !cornCoblinEntity.isEnemy(entity), CornCoblinBrain::setTargetInvalid, false),
						new FollowMobTask(mob -> isTarget(cornCoblinEntity, mob), (float)cornCoblinEntity.getAttributeValue(EntityAttributes.GENERIC_FOLLOW_RANGE)),
						new RangedApproachTask(1.2F),
						new MeleeAttackTask(18)
				),
				MemoryModuleType.ATTACK_TARGET
		);
	}

	private static void addEmergeActivities(Brain<CornCoblinEntity> brain) {
		brain.setTaskList(
				Activity.EMERGE,
				5,
				ImmutableList.of(
						new SHEmergeTask<>(EMERGE_DURATION)), MemoryModuleType.IS_EMERGING
		);
	}

	private static void addDigActivities(Brain<CornCoblinEntity> brain) {
		brain.setTaskList(
				Activity.DIG,
				ImmutableList.of(
						Pair.of(0, new ForceUnmountTask()),
						Pair.of(1, new SHDigTask<>(DIG_DURATION))),
				ImmutableSet.of(
						Pair.of(MemoryModuleType.ATTACK_TARGET, MemoryModuleState.VALUE_ABSENT), Pair.of(MemoryModuleType.DIG_COOLDOWN, MemoryModuleState.VALUE_ABSENT)
				)
		);
	}


	public static void updateActivities(CornCoblinEntity cornCoblinEntity) {
		cornCoblinEntity.getBrain().resetPossibleActivities(ImmutableList.of(Activity.FIGHT, Activity.IDLE, Activity.EMERGE, Activity.DIG));
		cornCoblinEntity.setAttacking(cornCoblinEntity.getBrain().hasMemoryModule(MemoryModuleType.ATTACK_TARGET));
	}

	public static void resetDigCooldown(LivingEntity livingEntity) {
		if (livingEntity.getBrain().hasMemoryModule(MemoryModuleType.DIG_COOLDOWN)) {
			livingEntity.getBrain().remember(MemoryModuleType.DIG_COOLDOWN, Unit.INSTANCE, 1200L);
		}

	}

	private static void setTargetInvalid(CornCoblinEntity cornCoblinEntity, LivingEntity target) {
		resetDigCooldown(cornCoblinEntity);
	}

	private static Optional<? extends LivingEntity> getAttackTarget(CornCoblinEntity cornCoblinEntity) {
		Brain<CornCoblinEntity> brain = cornCoblinEntity.getBrain();
		Optional<LivingEntity> optional = LookTargetUtil.getEntity(cornCoblinEntity, MemoryModuleType.ANGRY_AT);
		if(optional.isPresent() && Sensor.testAttackableTargetPredicateIgnoreVisibility(cornCoblinEntity, optional.get())){
			return optional;
		}
		if (brain.hasMemoryModule(MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER)) {
			return brain.getOptionalMemory(MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER);
		}
		if (brain.hasMemoryModule(MemoryModuleType.VISIBLE_MOBS)) {
			Optional<VisibleLivingEntitiesCache> visibleLivingEntitiesCache = cornCoblinEntity.getBrain().getOptionalMemory(MemoryModuleType.VISIBLE_MOBS);
			if(visibleLivingEntitiesCache.isPresent()){
				return visibleLivingEntitiesCache.get().method_38975(entity -> entity.getType() == EntityType.PLAYER && !entity.isSubmergedInWater());
			}
		}
		return Optional.empty();
	}

	private static boolean isTarget(CornCoblinEntity cornCoblinEntity, LivingEntity entity) {
		return cornCoblinEntity.getBrain().getOptionalMemory(MemoryModuleType.ATTACK_TARGET).filter(targetedEntity -> targetedEntity == entity).isPresent();
	}
}
