package dev.sterner.shuckhorror.common.entity;

import com.mojang.serialization.Dynamic;
import dev.sterner.shuckhorror.api.criteria.SHCriteria;
import dev.sterner.shuckhorror.common.entity.ai.CornCoblinBrain;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.UpdateAttackTargetTask;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.ProjectileDamageSource;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Unit;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;


public class CornCoblinEntity extends HostileEntity {

	public CornCoblinEntity(EntityType<? extends HostileEntity> entityType, World world) {
		super(entityType, world);
	}

	@Nullable
	@Override
	public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
		this.getBrain().remember(MemoryModuleType.DIG_COOLDOWN, Unit.INSTANCE, 300L);
		if (spawnReason != SpawnReason.CHUNK_GENERATION) {
			this.setPose(EntityPose.EMERGING);
			this.getBrain().remember(MemoryModuleType.IS_EMERGING, Unit.INSTANCE, CornCoblinBrain.EMERGE_DURATION);
		}
		return super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
	}

	@Override
	public Packet<?> createSpawnPacket() {
		return new EntitySpawnS2CPacket(this, this.isInPose(EntityPose.EMERGING) ? 1 : 0);
	}

	@Override
	public void onSpawnPacket(EntitySpawnS2CPacket packet) {
		super.onSpawnPacket(packet);
		if (packet.getEntityData() == 1) {
			this.setPose(EntityPose.EMERGING);
		}
	}


	@Override
	public boolean isPushable() {
		return !this.isDiggingOrEmerging() && super.isPushable();
	}

	@Override
	public boolean damage(DamageSource source, float amount) {
		boolean bl = super.damage(source, amount);
		if (!this.world.isClient && !this.isAiDisabled() && !this.isDiggingOrEmerging()) {
			Entity entity = source.getAttacker();
			if (this.brain.getOptionalMemory(MemoryModuleType.ATTACK_TARGET).isEmpty() && entity instanceof LivingEntity livingEntity && (!(source instanceof ProjectileDamageSource) || this.isInRange(livingEntity, 3.0))) {
				UpdateAttackTargetTask.updateAttackTarget(this, livingEntity);
			}
		}

		return bl;
	}

	@Override
	public void onTrackedDataSet(TrackedData<?> data) {
		if (POSE.equals(data)) {
			switch (this.getPose()) {
				//TODO case EMERGING ->  this.emergingAnimationState.start(this.age);
				//TODO case DIGGING ->   this.diggingAnimationState.start(this.age);
			}
		}

		super.onTrackedDataSet(data);
	}

	@Override
	public SoundCategory getSoundCategory() {
		return SoundCategory.HOSTILE;
	}

	@Override
	public boolean canBeLeashedBy(PlayerEntity player) {
		return false;
	}

	@Override
	public boolean isInvulnerableTo(DamageSource source) {
		return this.isDiggingOrEmerging() && !source.isOutOfWorld() || super.isInvulnerableTo(source);
	}

	private boolean isDiggingOrEmerging() {
		return this.isInPose(EntityPose.DIGGING) || this.isInPose(EntityPose.EMERGING);
	}

	protected void mobTick() {
		this.world.getProfiler().push("cornCoblinBrain");
		this.getBrain().tick((ServerWorld)this.world, this);
		this.world.getProfiler().pop();
		CornCoblinBrain.updateActivities(this);
		if(getTarget() instanceof PlayerEntity player){
			SHCriteria.ENTITY_TARGET_PLAYER.trigger((ServerPlayerEntity) player, this);
		}
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

	@SuppressWarnings("all")
	@Override
	public Brain<CornCoblinEntity> getBrain() {
		return (Brain<CornCoblinEntity>) super.getBrain();
	}

	public boolean isEnemy(LivingEntity entity) {//TODO
		if (this.world == entity.world
				&& EntityPredicates.EXCEPT_CREATIVE_OR_SPECTATOR.test(entity)
				&& !this.isTeammate(entity)
				&& entity.getType() != EntityType.ARMOR_STAND
				&& entity.getType() != EntityType.WARDEN
				&& !entity.isInvulnerable()
				&& !entity.isDead()
				&& this.world.getWorldBorder().contains(entity.getBoundingBox())) {
			return true;
		}

		return false;
	}
}
