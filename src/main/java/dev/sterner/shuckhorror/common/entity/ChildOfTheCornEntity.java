package dev.sterner.shuckhorror.common.entity;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import dev.sterner.shuckhorror.api.criteria.SHCriteria;
import dev.sterner.shuckhorror.client.network.packet.SpawnSoulEntityParticlesPacket;
import dev.sterner.shuckhorror.client.network.packet.SpawnSoulParticlesPacket;
import dev.sterner.shuckhorror.common.entity.ai.ChildOfTheCornBrain;
import dev.sterner.shuckhorror.common.registry.SHObjects;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class ChildOfTheCornEntity extends HostileEntity {
	protected static final TrackedData<Byte> CHILD_FLAGS = DataTracker.registerData(ChildOfTheCornEntity.class, TrackedDataHandlerRegistry.BYTE);
	private static final int CHARGING_FLAG = 1;
	public static final TrackedData<Integer> VARIANT = DataTracker.registerData(ChildOfTheCornEntity.class, TrackedDataHandlerRegistry.INTEGER);
	public ChildOfTheCornEntity(EntityType<? extends HostileEntity> entityType, World world) {
		super(entityType, world);
		this.moveControl = new CotCMoveControl(this);
	}

	@Override
	public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityTag) {
		int variants = getVariants();
		if (variants > 1) {
			dataTracker.set(VARIANT, random.nextInt(variants));
		}
		Random random = world.getRandom();
		this.initEquipment(random, difficulty);
		this.updateEnchantments(random, difficulty);
		return super.initialize(world, difficulty, spawnReason, entityData, entityTag);
	}


	@Override
	protected void initGoals() {
		super.initGoals();
		this.goalSelector.add(2, new MeleeAttackGoal(this, 1.0, false));
		this.goalSelector.add(3, new LookAroundGoal(this));
		this.goalSelector.add(4, new ChargeTargetGoal(this));
		this.goalSelector.add(5, new FlyRandomlyGoal());
		this.goalSelector.add(6, new LookAtTargetGoal(this));
		this.targetSelector.add(1, new RevengeGoal(this));
		this.targetSelector.add(3, new ActiveTargetGoal<>(this, PlayerEntity.class, true));

	}

	@Override
	protected void initEquipment(Random random, LocalDifficulty localDifficulty) {
		this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(SHObjects.SICKLE));
		this.setEquipmentDropChance(EquipmentSlot.MAINHAND, 50.0f);
	}

	private boolean areFlagsSet(int mask) {
		byte i = this.dataTracker.get(CHILD_FLAGS);
		return (i & mask) != 0;
	}

	private void setChildFlag(int mask, boolean value) {
		int i = this.dataTracker.get(CHILD_FLAGS);
		i = value ? (i |= mask) : (i &= ~mask);
		this.dataTracker.set(CHILD_FLAGS, (byte)(i & 0xFF));
	}

	public boolean isCharging() {
		return this.areFlagsSet(CHARGING_FLAG);
	}

	public void setCharging(boolean charging) {
		this.setChildFlag(CHARGING_FLAG, charging);
	}

	@Override
	protected void initDataTracker() {
		super.initDataTracker();
		if (getVariants() > 1) {
			dataTracker.startTracking(VARIANT, 1);
		}
		this.dataTracker.startTracking(CHILD_FLAGS, (byte)0);
	}

	@Override
	protected void updatePostDeath() {
		if (!this.world.isClient()) {
			PlayerLookup.tracking(this).forEach(trackingPlayer -> SpawnSoulEntityParticlesPacket.send(trackingPlayer, this));
		}
		super.updatePostDeath();
	}

	@Override
	public void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		if (getVariants() > 1) {
			dataTracker.set(VARIANT, nbt.getInt("Variant"));
		}
	}

	@Override
	public void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		if (getVariants() > 1) {
			nbt.putInt("Variant", dataTracker.get(VARIANT));
		}
	}

	public int getVariants() {
		return 5;
	}

	@Override
	public void move(MovementType movementType, Vec3d movement) {
		super.move(movementType, movement);
		this.checkBlockCollision();
	}

	@Override
	public void tick() {
		this.noClip = true;
		super.tick();
		this.noClip = false;
		this.setNoGravity(true);
	}

	protected void mobTick() {
		/*TODO Brain
		this.world.getProfiler().push("childOfTheCornBrain");
		this.getBrain().tick((ServerWorld)this.world, this);
		this.world.getProfiler().pop();
		ChildOfTheCornBrain.updateActivities(this);

		 */
		if(getTarget() instanceof PlayerEntity player){
			SHCriteria.ENTITY_TARGET_PLAYER.trigger((ServerPlayerEntity) player, this);

		}
		super.mobTick();
	}

	public static DefaultAttributeContainer.Builder createChildOfTheCornAttributes() {
		return MobEntity.createMobAttributes()
				.add(EntityAttributes.GENERIC_MAX_HEALTH, 14.0)
				.add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 1.0)
				.add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2.0)
				.add(EntityAttributes.GENERIC_FLYING_SPEED, 0.25);
	}

	public static boolean canSpawn(EntityType<ChildOfTheCornEntity> type, ServerWorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
		if(world.getDimension().getMoonPhase(world.getLunarTime()) == 4){
			return world.toServerWorld().isNight() && HostileEntity.canSpawnInDark(type, world, spawnReason, pos, random);
		}else{
			return world.getRandom().nextBoolean() && world.toServerWorld().isNight() && HostileEntity.canSpawnInDark(type, world, spawnReason, pos, random);
		}
	}

	/* TODO Brain
	@Override
	protected Brain<?> deserializeBrain(Dynamic<?> dynamic) {
		return ChildOfTheCornBrain.create(this, dynamic);
	}
	@Override
	public Brain<ChildOfTheCornEntity> getBrain() {
		return (Brain<ChildOfTheCornEntity>) super.getBrain();
	}

	 */
	public static class CotCMoveControl extends MoveControl {
		private ChildOfTheCornEntity entity;
		public CotCMoveControl(ChildOfTheCornEntity owner) {
			super(owner);
			this.entity = owner;
		}

		@Override
		public void tick() {
			if (this.state != MoveControl.State.MOVE_TO) {
				return;
			}
			Vec3d vec3d = new Vec3d(this.targetX - entity.getX(), this.targetY - entity.getY(), this.targetZ - entity.getZ());
			double d = vec3d.length();
			if (d < entity.getBoundingBox().getAverageSideLength()) {
				this.state = MoveControl.State.WAIT;
				entity.setVelocity(entity.getVelocity().multiply(0.5));
			} else {
				entity.setVelocity(entity.getVelocity().add(vec3d.multiply(this.speed * 0.05 / d)));
				if (entity.getTarget() == null) {
					Vec3d vec3d2 = entity.getVelocity();
					entity.setYaw(-((float)MathHelper.atan2(vec3d2.x, vec3d2.z)) * 57.295776f);
					entity.bodyYaw = entity.getYaw();
				} else {
					double e = entity.getTarget().getX() - entity.getX();
					double f = entity.getTarget().getZ() - entity.getZ();
					entity.setYaw(-((float)MathHelper.atan2(e, f)) * 57.295776f);
					entity.bodyYaw = entity.getYaw();
				}
			}
		}
	}
	/*
	public static class CotCMoveControl extends MoveControl {
		private ChildOfTheCornEntity entity;
		public CotCMoveControl(ChildOfTheCornEntity owner) {
			super(owner);
			this.entity = owner;
		}

		@Override
		public void tick() {
			if (state == MoveControl.State.MOVE_TO && (entity.getTarget() == null || (!entity.canSee(entity.getTarget())))) {
				Vec3d targetPosition = new Vec3d(targetX - entity.getX(), targetY - entity.getY(), targetZ - entity.getZ());
				double distance = targetPosition.length();
				if (distance < entity.getBoundingBox().getAverageSideLength()) {
					state = MoveControl.State.WAIT;
					entity.setVelocity(entity.getVelocity().multiply(0.5));
				} else {
					entity.setVelocity(entity.getVelocity().add(targetPosition.multiply(speed * 0.05 / distance)));
					if (entity.getTarget() == null) {
						Vec3d velocity = entity.getVelocity();
						//noinspection SuspiciousNameCombination
						entity.setYaw(-((float) MathHelper.atan2(velocity.x, velocity.z)) * 57);
					} else {
						entity.setYaw(-((float) MathHelper.atan2(entity.getTarget().getX() - entity.getX(), entity.getTarget().getZ() - entity.getZ())) * 57);
					}
					entity.bodyYaw = entity.getYaw();
				}
			}
		}
	}

	 */

	private class FlyRandomlyGoal extends Goal {
		public FlyRandomlyGoal() {
			this.setControls(EnumSet.of(Goal.Control.MOVE));
		}

		@Override
		public boolean canStart() {
			if (getTarget() == null || !canSee(getTarget())) {
				MoveControl moveControl = getMoveControl();
				if (!moveControl.isMoving()) {
					return true;
				} else {
					double distanceX = moveControl.getTargetX() - getX();
					double distanceY = moveControl.getTargetY() - getY();
					double distanceZ = moveControl.getTargetZ() - getZ();
					double fin = distanceX * distanceX + distanceY * distanceY + distanceZ * distanceZ;
					return fin < 1 || fin > 3600;
				}
			}
			return false;
		}

		@Override
		public boolean shouldContinue() {
			return false;
		}

		@Override
		public void start() {
			BlockPos target = findTarget(new BlockPos.Mutable(getX() + MathHelper.nextDouble(random, -8, 8), getY() + MathHelper.nextDouble(random, -4, 4), getZ() + MathHelper.nextDouble(random, -8, 8)), 0);
			if (target != null) {
				getMoveControl().moveTo(target.getX(), target.getY(), target.getZ(), 0.2);
				lookAt(EntityAnchorArgumentType.EntityAnchor.EYES, new Vec3d(target.getX(), target.getY(), target.getZ()));
			}
		}

		private BlockPos findTarget(BlockPos.Mutable target, int tries) {
			if (tries <= 8) {
				while (isInsideBuildLimit(world, target) && world.getBlockState(target).getMaterial().isSolid()) {
					target.set(target.getX(), target.getY() + 1, target.getZ());
				}
				while (isInsideBuildLimit(world, target) && !world.getBlockState(target).getMaterial().isSolid()) {
					target.set(target.getX(), target.getY() - 1, target.getZ());
				}
				target.set(target.getX(), target.getY() + random.nextInt(8), target.getZ());
				return target.toImmutable();
			}
			return null;
		}

		private static boolean isInsideBuildLimit(World world, BlockPos pos) {
			return pos.getY() >= world.getBottomY() && pos.getY() <= world.getTopY();
		}
	}

	public static class LookAtTargetGoal extends Goal {
		private ChildOfTheCornEntity childOfTheCornEntity;
		public LookAtTargetGoal(ChildOfTheCornEntity childOfTheCornEntity) {
			this.setControls(EnumSet.of(Goal.Control.MOVE));
			this.childOfTheCornEntity = childOfTheCornEntity;
		}

		@Override
		public boolean canStart() {
			return !childOfTheCornEntity.getMoveControl().isMoving() && childOfTheCornEntity.random.nextInt(LookAtTargetGoal.toGoalTicks(7)) == 0;
		}

		@Override
		public boolean shouldContinue() {
			return false;
		}

		@Override
		public void tick() {
			BlockPos blockPos = childOfTheCornEntity.getBlockPos();
			for (int i = 0; i < 3; ++i) {
				BlockPos blockPos2 = blockPos.add(childOfTheCornEntity.random.nextInt(15) - 7, childOfTheCornEntity.random.nextInt(11) - 5, childOfTheCornEntity.random.nextInt(15) - 7);
				if (!childOfTheCornEntity.world.isAir(blockPos2)) continue;
				childOfTheCornEntity.moveControl.moveTo((double)blockPos2.getX() + 0.5, (double)blockPos2.getY() + 0.5, (double)blockPos2.getZ() + 0.5, 0.25);
				if (childOfTheCornEntity.getTarget() != null) break;
				childOfTheCornEntity.getLookControl().lookAt((double)blockPos2.getX() + 0.5, (double)blockPos2.getY() + 0.5, (double)blockPos2.getZ() + 0.5, 180.0f, 20.0f);
				break;
			}
		}
	}
	class ChargeTargetGoal extends Goal {
		private ChildOfTheCornEntity childOfTheCornEntity;
		public ChargeTargetGoal(ChildOfTheCornEntity childOfTheCornEntity) {
			this.setControls(EnumSet.of(Goal.Control.MOVE));
			this.childOfTheCornEntity = childOfTheCornEntity;
		}

		@Override
		public boolean canStart() {
			LivingEntity livingEntity = childOfTheCornEntity.getTarget();
			if (livingEntity != null && livingEntity.isAlive() && !childOfTheCornEntity.getMoveControl().isMoving() && childOfTheCornEntity.random.nextInt(ChargeTargetGoal.toGoalTicks(7)) == 0) {
				return childOfTheCornEntity.squaredDistanceTo(livingEntity) > 4.0;
			}
			return false;
		}

		@Override
		public boolean shouldContinue() {
			return childOfTheCornEntity.getMoveControl().isMoving() && childOfTheCornEntity.isCharging() && childOfTheCornEntity.getTarget() != null && childOfTheCornEntity.getTarget().isAlive();
		}

		@Override
		public void start() {
			LivingEntity livingEntity = childOfTheCornEntity.getTarget();
			if (livingEntity != null) {
				Vec3d vec3d = livingEntity.getEyePos();
				childOfTheCornEntity.moveControl.moveTo(vec3d.x, vec3d.y, vec3d.z, 1.0);
			}
			childOfTheCornEntity.setCharging(true);
			childOfTheCornEntity.playSound(SoundEvents.ENTITY_VEX_CHARGE, 1.0f, 1.0f);
		}

		@Override
		public void stop() {
			childOfTheCornEntity.setCharging(false);
		}

		@Override
		public boolean shouldRunEveryTick() {
			return true;
		}

		@Override
		public void tick() {
			LivingEntity livingEntity = childOfTheCornEntity.getTarget();
			if (livingEntity == null) {
				return;
			}
			if (childOfTheCornEntity.getBoundingBox().intersects(livingEntity.getBoundingBox())) {
				childOfTheCornEntity.tryAttack(livingEntity);
				childOfTheCornEntity.setCharging(false);
			} else {
				double d = childOfTheCornEntity.squaredDistanceTo(livingEntity);
				if (d < 9.0) {
					Vec3d vec3d = livingEntity.getEyePos();
					childOfTheCornEntity.moveControl.moveTo(vec3d.x, vec3d.y, vec3d.z, 1.0);
				}
			}
		}
	}
}
