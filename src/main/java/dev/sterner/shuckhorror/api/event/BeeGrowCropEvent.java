package dev.sterner.shuckhorror.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;

import static net.fabricmc.fabric.impl.base.event.EventFactoryImpl.createArrayBacked;

public class BeeGrowCropEvent {
	public static final Event<BeeGrowCropEvent.OnGrow> ON_BEE_GROW_CROP = createArrayBacked(BeeGrowCropEvent.OnGrow.class, listeners -> (entity, blockState, blockPos) -> {
		for (BeeGrowCropEvent.OnGrow listener : listeners) {
			listener.onGrow(entity,blockState, blockPos);
		}
	});

	public static final Event<BeeGrowCropEvent.OnPollinate> ON_BEE_POLLINATE = createArrayBacked(BeeGrowCropEvent.OnPollinate.class, listeners -> (entity, blockState, blockPos) -> {
		for (BeeGrowCropEvent.OnPollinate listener : listeners) {
			listener.onPollinate(entity,blockState, blockPos);
		}
	});

	@FunctionalInterface
	public interface OnGrow {
		void onGrow(LivingEntity entity, BlockState blockState, BlockPos blockPos);
	}

	@FunctionalInterface
	public interface OnPollinate {
		void onPollinate(LivingEntity entity, BlockState blockState, BlockPos blockPos);
	}
}
