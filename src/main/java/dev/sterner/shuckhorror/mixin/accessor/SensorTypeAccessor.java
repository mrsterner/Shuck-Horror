package dev.sterner.shuckhorror.mixin.accessor;

import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import java.util.function.Supplier;

@SuppressWarnings("ALL")

@Mixin(SensorType.class)
public interface SensorTypeAccessor {

	@Invoker
	static <U extends Sensor<?>> SensorType<U> callRegister(String string, Supplier<U> supplier) {
		throw new UnsupportedOperationException();
	}

}
