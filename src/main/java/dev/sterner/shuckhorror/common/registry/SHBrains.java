package dev.sterner.shuckhorror.common.registry;

import dev.sterner.shuckhorror.common.entity.ai.sensor.CornCoblinSpecificSensor;
import dev.sterner.shuckhorror.common.util.Constants;
import dev.sterner.shuckhorror.mixin.accessor.SensorTypeAccessor;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.GlobalPos;
import net.minecraft.util.registry.Registry;

import java.util.*;
import java.util.function.Supplier;

public class SHBrains {
	//MEMORY
	public static final MemoryModuleType<List<GlobalPos>> TARGET_BLOCKS = register("target_blocks");

	//SENSOR
	public static final SensorType<CornCoblinSpecificSensor> CORN_COBLIN_SPECIFIC_SENSOR = register("corn_coblin_specific_sensor", CornCoblinSpecificSensor::new);


	private static <U> MemoryModuleType<U> register(String id) {
		return Registry.register(Registry.MEMORY_MODULE_TYPE, new Identifier(id), new MemoryModuleType<>(Optional.empty()));
	}

	private static <U extends Sensor<?>> SensorType<U> register(String id, Supplier<U> factory) {
		return SensorTypeAccessor.callRegister(Constants.MODID + ":" + id, factory);
	}

	public static void init() {

	}
}
