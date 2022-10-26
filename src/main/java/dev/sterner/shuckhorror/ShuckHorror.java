package dev.sterner.shuckhorror;

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
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;

public class ShuckHorror implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("Shuck Horror");

	@Override
	public void onInitialize() {
		SHObjects.init();
		SHEntityTypes.init();
		SHWorldGenerators.init();
		SHBrains.init();
		SHCriteria.init();
		EntityDeathEvent.ON_ENTITY_DEATH.register(this::curseCornOnDeath);
		UseBlockCallback.EVENT.register(this::onScytheHarvest);
	}



	private ActionResult onScytheHarvest(PlayerEntity playerEntity, World world, Hand hand, BlockPos pos){
		if(hand == Hand.MAIN_HAND && playerEntity.getStackInHand(hand).isOf(SHObjects.SCYTHE)){
			if(world.getBlockState(pos).isIn(Constants.Tags.CORN)){
				BlockState blockState = world.getBlockState(pos);
				for(int x = -1; x < 2; x++){
					for(int z = -1; z < 2; z++){
						BlockPos newPos = pos.add(x,0 ,z);
						if(world.getBlockState(newPos).isIn(Constants.Tags.CORN)){
							SHUtils.harvestCorn(playerEntity, world, blockState, newPos);
						}
					}
				}
				playerEntity.swingHand(hand);
				return ActionResult.CONSUME;
			}
		}
		return ActionResult.PASS;
	}

	private ActionResult onScytheHarvest(PlayerEntity playerEntity, World world, Hand hand, BlockHitResult blockHitResult) {
		return onScytheHarvest(playerEntity, world, hand, blockHitResult.getBlockPos());
	}


	private void curseCornOnDeath(LivingEntity livingEntity, BlockPos blockPos) {
		int range = 2;
		for(int i = -range; i < range; i++){
			for(int j = -range; j < range; j++){
				for(int k = -range; k < range; k++){
					blockPos.add(i ,j ,k);
					if (livingEntity.world.getBlockState(blockPos).isOf(SHObjects.CORN_CROP)) {
						SHUtils.transferBlockState(livingEntity.world, blockPos, SHObjects.CURSED_CORN_CROP.getDefaultState());
						if (livingEntity.world.getBlockState(blockPos.up()).isOf(SHObjects.CORN_CROP)) {
							SHUtils.transferBlockState(livingEntity.world, blockPos.up(), SHObjects.CURSED_CORN_CROP.getDefaultState());
						}
						if (livingEntity.world.getBlockState(blockPos.down()).isOf(SHObjects.CORN_CROP)) {
							SHUtils.transferBlockState(livingEntity.world, blockPos.down(), SHObjects.CURSED_CORN_CROP.getDefaultState());
						}

					}
				}
			}
		}

	}
}

