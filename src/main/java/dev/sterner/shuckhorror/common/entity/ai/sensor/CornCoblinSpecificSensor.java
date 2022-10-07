package dev.sterner.shuckhorror.common.entity.ai.sensor;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import dev.sterner.shuckhorror.common.registry.SHBrains;
import dev.sterner.shuckhorror.common.util.Constants;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.dynamic.GlobalPos;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

import java.util.List;
import java.util.Set;

public class CornCoblinSpecificSensor extends Sensor<LivingEntity> {
	private static final int RUN_TIME = 40;

	public CornCoblinSpecificSensor() {
		super(40);
	}

	protected void sense(ServerWorld serverWorld, LivingEntity livingEntity) {
		RegistryKey<World> registryKey = serverWorld.getRegistryKey();
		BlockPos blockPos = livingEntity.getBlockPos();
		List<GlobalPos> list = Lists.newArrayList();

		for(int j = -4; j <= 4; ++j) {
			for(int k = -2; k <= 2; ++k) {
				for(int l = -4; l <= 4; ++l) {
					BlockPos blockPos2 = blockPos.add(j, k, l);
					if (serverWorld.getBlockState(blockPos2).isIn(Constants.Tags.CORN_COBLING_BLOCK_DESTROYABLE)) {
						list.add(GlobalPos.create(registryKey, blockPos2));
					}
				}
			}
		}

		Brain<?> brain = livingEntity.getBrain();
		if (!list.isEmpty()) {
			brain.remember(SHBrains.TARGET_BLOCKS, list);
		} else {
			brain.forget(SHBrains.TARGET_BLOCKS);
		}

	}

	@Override
	public Set<MemoryModuleType<?>> getOutputMemoryModules() {
		return ImmutableSet.of(SHBrains.TARGET_BLOCKS);
	}
}
