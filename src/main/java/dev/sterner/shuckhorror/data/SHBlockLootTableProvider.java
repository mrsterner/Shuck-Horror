package dev.sterner.shuckhorror.data;

import dev.sterner.shuckhorror.common.block.CornCropBlock;
import dev.sterner.shuckhorror.common.block.TallCropBlock;
import dev.sterner.shuckhorror.common.registry.SHObjects;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.BlockStatePropertyLootCondition;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.ApplyBonusLootFunction;
import net.minecraft.predicate.StatePredicate;

public class SHBlockLootTableProvider  extends FabricBlockLootTableProvider {
	protected SHBlockLootTableProvider(FabricDataGenerator dataGenerator) {
		super(dataGenerator);
	}

	@Override
	protected void generateBlockLootTables() {
		LootCondition.Builder cornBuilder4 = BlockStatePropertyLootCondition.builder(SHObjects.CORN_CROP).properties(StatePredicate.Builder.create().exactMatch(CornCropBlock.AGE, 4).exactMatch(TallCropBlock.HALF, DoubleBlockHalf.UPPER));
		LootCondition.Builder cornBuilder5 = BlockStatePropertyLootCondition.builder(SHObjects.CORN_CROP).properties(StatePredicate.Builder.create().exactMatch(CornCropBlock.AGE, 5).exactMatch(TallCropBlock.HALF, DoubleBlockHalf.UPPER));

		LootCondition.Builder cursedCornBuilder4 = BlockStatePropertyLootCondition.builder(SHObjects.CURSED_CORN_CROP).properties(StatePredicate.Builder.create().exactMatch(CornCropBlock.AGE, 4).exactMatch(TallCropBlock.HALF, DoubleBlockHalf.UPPER));
		LootCondition.Builder cursedCornBuilder5 = BlockStatePropertyLootCondition.builder(SHObjects.CURSED_CORN_CROP).properties(StatePredicate.Builder.create().exactMatch(CornCropBlock.AGE, 5).exactMatch(TallCropBlock.HALF, DoubleBlockHalf.UPPER));

		LootCondition.Builder candyCornBuilder4 = BlockStatePropertyLootCondition.builder(SHObjects.CANDY_CORN_CROP).properties(StatePredicate.Builder.create().exactMatch(CornCropBlock.AGE, 4).exactMatch(TallCropBlock.HALF, DoubleBlockHalf.UPPER));
		LootCondition.Builder candyCornBuilder5 = BlockStatePropertyLootCondition.builder(SHObjects.CANDY_CORN_CROP).properties(StatePredicate.Builder.create().exactMatch(CornCropBlock.AGE, 5).exactMatch(TallCropBlock.HALF, DoubleBlockHalf.UPPER));

		this.addDrop(SHObjects.WILD_MAIZE, SHObjects.CORN_KERNELS);

		this.addDrop(SHObjects.CORN_CROP, applyExplosionDecay(
				SHObjects.CORN_CROP,
				LootTable.builder()
						.pool(LootPool.builder()
								.with(ItemEntry.builder(SHObjects.CORN_KERNELS)))
						.pool(LootPool.builder()
								.conditionally(cornBuilder4)
								.with(ItemEntry.builder(SHObjects.CORN_COB_1)))
						.pool(LootPool.builder()
								.conditionally(cornBuilder5)
								.with(ItemEntry.builder(SHObjects.CORN_COB_1)
										.apply(ApplyBonusLootFunction.binomialWithBonusCount(Enchantments.FORTUNE, 0.5714286F, 2))))
								)
						);

		this.addDrop(SHObjects.CANDY_CORN_CROP, applyExplosionDecay(
						SHObjects.CANDY_CORN_CROP,
						LootTable.builder()
								.pool(LootPool.builder()
										.with(ItemEntry.builder(SHObjects.CORN_KERNELS)))
								.pool(LootPool.builder()
										.conditionally(candyCornBuilder4)
										.with(ItemEntry.builder(SHObjects.CANDY_CORN)))
								.pool(LootPool.builder()
										.conditionally(candyCornBuilder5)
										.with(ItemEntry.builder(SHObjects.CANDY_CORN)
												.apply(ApplyBonusLootFunction.binomialWithBonusCount(Enchantments.FORTUNE, 0.5714286F, 2))))
				)
		);

		this.addDrop(SHObjects.CURSED_CORN_CROP, applyExplosionDecay(
						SHObjects.CURSED_CORN_CROP,
						LootTable.builder()
								.pool(LootPool.builder()
										.with(ItemEntry.builder(SHObjects.CURSED_CORN_KERNELS)))
								.pool(LootPool.builder()
										.conditionally(cursedCornBuilder4)
										.with(ItemEntry.builder(SHObjects.CURSED_CORN_1)))
								.pool(LootPool.builder()
										.conditionally(cursedCornBuilder5)
										.with(ItemEntry.builder(SHObjects.CURSED_CORN_1)))
				)
		);
	}


}
