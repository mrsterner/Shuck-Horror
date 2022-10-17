package dev.sterner.shuckhorror;

import dev.sterner.shuckhorror.api.event.EntityDeathEvent;
import dev.sterner.shuckhorror.common.registry.SHEntityTypes;
import dev.sterner.shuckhorror.common.registry.SHBrains;
import dev.sterner.shuckhorror.common.registry.SHObjects;
import dev.sterner.shuckhorror.common.registry.SHWorldGenerators;
import dev.sterner.shuckhorror.common.util.SHUtils;
import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShuckHorror implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("Shuck Horror");

	@Override
	public void onInitialize() {
		SHObjects.init();
		SHEntityTypes.init();
		SHWorldGenerators.init();
		SHBrains.init();

		EntityDeathEvent.ON_ENTITY_DEATH.register(this::curseCornOnDeath);
	}

	/**
	 * make cursed corn when something dies on corn
	 *
	 * @param livingEntity dying entity
	 * @param blockPos     hopefully on corn
	 */
	private void curseCornOnDeath(LivingEntity livingEntity, BlockPos blockPos) {
		if (livingEntity.world.getBlockState(blockPos).isOf(SHObjects.CORN_CROP)) {
			SHUtils.transferBlockState(livingEntity.world, blockPos);
			if (livingEntity.world.getBlockState(blockPos.up()).isOf(SHObjects.CORN_CROP)) {
				SHUtils.transferBlockState(livingEntity.world, blockPos.up());
			}
			if (livingEntity.world.getBlockState(blockPos.down()).isOf(SHObjects.CORN_CROP)) {
				SHUtils.transferBlockState(livingEntity.world, blockPos.down());
			}
		}
	}
}

