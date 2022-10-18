package dev.sterner.shuckhorror.data;

import dev.sterner.shuckhorror.common.registry.SHObjects;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.tag.BlockTags;

public class SHBlockTagProvider extends FabricTagProvider.BlockTagProvider {


	public SHBlockTagProvider(FabricDataGenerator dataGenerator) {
		super(dataGenerator);
	}

	@Override
	protected void generateTags() {
		getOrCreateTagBuilder(BlockTags.OCCLUDES_VIBRATION_SIGNALS).add(SHObjects.CURSED_CORN_CROP);
		getOrCreateTagBuilder(BlockTags.BEE_GROWABLES).add(SHObjects.CURSED_CORN_CROP);
	}
}
