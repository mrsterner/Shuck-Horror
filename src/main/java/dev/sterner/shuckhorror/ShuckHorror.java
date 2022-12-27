package dev.sterner.shuckhorror;

import com.mojang.authlib.GameProfile;
import dev.sterner.shuckhorror.api.criteria.SHCriteria;
import dev.sterner.shuckhorror.api.event.EntityDeathEvent;
import dev.sterner.shuckhorror.common.block.TallCropBlock;
import dev.sterner.shuckhorror.common.registry.SHEntityTypes;
import dev.sterner.shuckhorror.common.registry.SHBrains;
import dev.sterner.shuckhorror.common.registry.SHObjects;
import dev.sterner.shuckhorror.common.registry.SHWorldGenerators;
import dev.sterner.shuckhorror.common.util.Constants;
import dev.sterner.shuckhorror.common.util.SHUtils;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.block.TallPlantBlock;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.mob.WitherSkeletonEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ShuckHorror implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("Shuck Horror");

	@Override
	public void onInitialize() {
		SHObjects.init();
		SHEntityTypes.init();
		SHWorldGenerators.init();
		SHBrains.init();
		SHCriteria.init();
		ServerLivingEntityEvents.AFTER_DEATH.register(this::curseCornOnDeath);
		ServerLivingEntityEvents.AFTER_DEATH.register(this::onGrimReap);
		UseBlockCallback.EVENT.register(this::onScytheHarvest);
		AttackBlockCallback.EVENT.register(this::onScytheHarvest);

	}

	private void curseCornOnDeath(LivingEntity livingEntity, DamageSource damageSource) {
		BlockPos blockPos = livingEntity.getBlockPos();

		BlockPos origo = blockPos.add(-1,0,-1);
		List<BlockPos> listPos = new ArrayList<>();
		for(int x = 0; x < 3; x++){
			for(int z = 0; z < 3; z++){
				for (int y = 0; y < 3; y++){
					BlockPos checkPos = origo.add(x,y,z);
					if(livingEntity.world.getBlockState(checkPos).isOf(SHObjects.CORN_CROP) && livingEntity.world.getRandom().nextBoolean()){
						listPos.add(checkPos);
					}

				}
			}
		}
		listPos.forEach(blockPos1 -> {
			SHUtils.transferBlockState(livingEntity.world, blockPos1, SHObjects.CURSED_CORN_CROP.getDefaultState());
			if (livingEntity.world.getBlockState(blockPos1.up()).isOf(SHObjects.CORN_CROP)) {
				SHUtils.transferBlockState(livingEntity.world, blockPos1.up(), SHObjects.CURSED_CORN_CROP.getDefaultState());
			}
			if (livingEntity.world.getBlockState(blockPos1.down()).isOf(SHObjects.CORN_CROP)) {
				SHUtils.transferBlockState(livingEntity.world, blockPos1.down(), SHObjects.CURSED_CORN_CROP.getDefaultState());
			}
		});
	}

	private void onGrimReap(LivingEntity livingEntity, DamageSource source) {
		if(source.getAttacker() instanceof ServerPlayerEntity playerEntity){
			if(playerEntity.getMainHandStack().isOf(SHObjects.SCYTHE) || playerEntity.getMainHandStack().isOf(SHObjects.SICKLE)){
				if(playerEntity.getWorld().getRandom().nextDouble() < 0.1){
					if (livingEntity instanceof CreeperEntity) {
						livingEntity.dropItem(Items.CREEPER_HEAD);
						SHCriteria.HIT_ENTITY_WITH_ITEM.trigger(playerEntity, playerEntity.getMainHandStack(), livingEntity, source);
					}
					else if (livingEntity instanceof ZombieEntity) {
						livingEntity.dropItem(Items.ZOMBIE_HEAD);
						SHCriteria.HIT_ENTITY_WITH_ITEM.trigger(playerEntity, playerEntity.getMainHandStack(), livingEntity, source);
					}
					else if (livingEntity instanceof SkeletonEntity) {
						livingEntity.dropItem(Items.SKELETON_SKULL);
						SHCriteria.HIT_ENTITY_WITH_ITEM.trigger(playerEntity, playerEntity.getMainHandStack(), livingEntity, source);
					}
					else if (livingEntity instanceof WitherSkeletonEntity) {
						livingEntity.dropItem(Items.WITHER_SKELETON_SKULL);
						SHCriteria.HIT_ENTITY_WITH_ITEM.trigger(playerEntity, playerEntity.getMainHandStack(), livingEntity, source);
					}
					else if (livingEntity instanceof PlayerEntity player) {
						ItemStack stack = new ItemStack(Items.PLAYER_HEAD);
						SHCriteria.HIT_ENTITY_WITH_ITEM.trigger(playerEntity, playerEntity.getMainHandStack(), livingEntity, source);
						GameProfile gameProfile = player.getGameProfile();
						stack.getOrCreateNbt().put("SkullOwner", NbtHelper.writeGameProfile(new NbtCompound(), gameProfile));
						livingEntity.dropStack(stack);
					}
				}
			}
		}
	}

	private ActionResult newHarvestMethod(PlayerEntity playerEntity, World world, BlockPos pos, int range){
		if (!world.isClient) {
			for (int x = -range; x <= range; x++) {
				for (int z = -range; z <= range; z++) {
					BlockPos newPos = pos.add(x, 0, z);
					BlockState state = world.getBlockState(newPos);
					if (state.getBlock() instanceof TallCropBlock tallCropBlock && tallCropBlock.isMature(state) && state.get(TallCropBlock.HALF) == DoubleBlockHalf.UPPER) {
						SHUtils.harvestCorn(playerEntity, world, state, newPos);
					} else if (state.getBlock() instanceof CropBlock cropBlock && cropBlock.isMature(state) && !state.isIn(Constants.Tags.CORN)) {
						world.setBlockState(newPos, SHUtils.replant(state), Block.NOTIFY_LISTENERS);
						world.emitGameEvent(GameEvent.BLOCK_CHANGE, newPos, GameEvent.Emitter.of(playerEntity, state));
						Block.dropStacks(state, world, newPos, null, playerEntity, playerEntity.getMainHandStack());
					}
				}
			}
			playerEntity.swingHand(Hand.MAIN_HAND);
			return ActionResult.CONSUME;
		}
		return ActionResult.PASS;

	}

	private ActionResult onScytheHarvest(PlayerEntity playerEntity, World world, Hand hand, BlockPos blockPos, Direction direction) {
		if(hand == Hand.MAIN_HAND && playerEntity.getStackInHand(hand).isOf(SHObjects.SCYTHE)) {
			return newHarvestMethod(playerEntity, world, blockPos, 2);
		}
		if(hand == Hand.MAIN_HAND && playerEntity.getStackInHand(hand).isOf(SHObjects.SICKLE)) {
			return newHarvestMethod(playerEntity, world, blockPos, 1);
		}
		return ActionResult.PASS;
	}

	private ActionResult onScytheHarvest(PlayerEntity playerEntity, World world, Hand hand, BlockHitResult blockHitResult) {
		if(hand == Hand.MAIN_HAND && playerEntity.getStackInHand(hand).isOf(SHObjects.SCYTHE)) {
			return newHarvestMethod(playerEntity, world, blockHitResult.getBlockPos(), 2);
		}
		if(hand == Hand.MAIN_HAND && playerEntity.getStackInHand(hand).isOf(SHObjects.SICKLE)) {
			return newHarvestMethod(playerEntity, world, blockHitResult.getBlockPos(), 1);
		}
		return ActionResult.PASS;
	}
}

