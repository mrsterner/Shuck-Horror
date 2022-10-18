package dev.sterner.shuckhorror.data;

import dev.sterner.shuckhorror.common.block.CornBreadBlock;
import dev.sterner.shuckhorror.common.registry.SHObjects;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.*;

public class SHModelProvider extends FabricModelProvider {
	public SHModelProvider(FabricDataGenerator dataGenerator) {
		super(dataGenerator);
	}

	@Override
	public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
		blockStateModelGenerator.registerTintableCross(SHObjects.WILD_MAIZE, BlockStateModelGenerator.TintType.TINTED);
		registerCake(blockStateModelGenerator);
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
		itemModelGenerator.register(SHObjects.CORN_BREAD, Models.GENERATED);

	}

	private void registerCake(BlockStateModelGenerator blockStateModelGenerator) {
		blockStateModelGenerator.blockStateCollector
				.accept(VariantsBlockStateSupplier
						.create(SHObjects.CORN_BREAD_BLOCK)
						.coordinate(BlockStateVariantMap
								.create(CornBreadBlock.CORN_BITES)
								.register(0, BlockStateVariant.create().put(VariantSettings.MODEL, ModelIds.getBlockModelId(SHObjects.CORN_BREAD_BLOCK)))
								.register(1, BlockStateVariant.create().put(VariantSettings.MODEL, ModelIds.getBlockSubModelId(SHObjects.CORN_BREAD_BLOCK, "_slice1")))
								.register(2, BlockStateVariant.create().put(VariantSettings.MODEL, ModelIds.getBlockSubModelId(SHObjects.CORN_BREAD_BLOCK, "_slice2")))
								.register(3, BlockStateVariant.create().put(VariantSettings.MODEL, ModelIds.getBlockSubModelId(SHObjects.CORN_BREAD_BLOCK, "_slice3")))));
	}
}
