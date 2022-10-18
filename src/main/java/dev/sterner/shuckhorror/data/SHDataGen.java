package dev.sterner.shuckhorror.data;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class SHDataGen implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		fabricDataGenerator.addProvider(SHModelProvider::new);
		fabricDataGenerator.addProvider(SHBlockTagProvider::new);
		fabricDataGenerator.addProvider(SHRecipeProvider::new);
		fabricDataGenerator.addProvider(SHBlockLootTableProvider::new);
	}
}
