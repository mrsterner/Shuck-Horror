package dev.sterner.shuckhorror;

import dev.sterner.shuckhorror.common.registry.SHEntityTypes;
import dev.sterner.shuckhorror.common.registry.SHObjects;
import dev.sterner.shuckhorror.common.registry.SHWorldGenerators;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShuckHorror implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("Shuck Horror");

	@Override
	public void onInitialize(ModContainer mod) {
		SHObjects.init();
		SHEntityTypes.init();
		SHWorldGenerators.init();
	}
}
