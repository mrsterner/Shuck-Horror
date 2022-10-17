package dev.sterner.shuckhorror.data;

import dev.sterner.shuckhorror.common.registry.SHObjects;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.model.BlockStateModelGenerator;
import net.minecraft.data.client.model.Model;
import net.minecraft.data.client.model.Models;
import net.minecraft.state.property.Properties;

public class SHModelProvider extends FabricModelProvider {
	public SHModelProvider(FabricDataGenerator dataGenerator) {
		super(dataGenerator);
	}

	@Override
	public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
		blockStateModelGenerator.registerTintableCross(SHObjects.WILD_MAIZE, BlockStateModelGenerator.TintType.TINTED);
	}

	@Override
	public void generateItemModels(ItemModelGenerator itemModelGenerator) {
		itemModelGenerator.register(SHObjects.CORN_COB_1, Models.GENERATED);
		itemModelGenerator.register(SHObjects.CORN_COB_2, Models.GENERATED);
		itemModelGenerator.register(SHObjects.CORN_COB_3, Models.GENERATED);

		itemModelGenerator.register(SHObjects.CURSED_CORN_1, Models.GENERATED);
		itemModelGenerator.register(SHObjects.CURSED_CORN_2, Models.GENERATED);
		itemModelGenerator.register(SHObjects.CURSED_CORN_3, Models.GENERATED);

		itemModelGenerator.register(SHObjects.ROASTED_CORN_1, Models.GENERATED);
		itemModelGenerator.register(SHObjects.ROASTED_CORN_2, Models.GENERATED);
		itemModelGenerator.register(SHObjects.ROASTED_CORN_3, Models.GENERATED);

		itemModelGenerator.register(SHObjects.CANDY_CORN, Models.GENERATED);

		itemModelGenerator.register(SHObjects.CORN_KERNELS, Models.GENERATED);
		itemModelGenerator.register(SHObjects.CURSED_CORN_KERNELS, Models.GENERATED);
		itemModelGenerator.register(SHObjects.GARMONBOZIA, Models.GENERATED);
		itemModelGenerator.register(SHObjects.POPCORN, Models.GENERATED);
		itemModelGenerator.register(SHObjects.SICKLE, Models.HANDHELD);

	}
}
