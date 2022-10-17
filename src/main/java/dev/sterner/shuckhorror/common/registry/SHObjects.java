package dev.sterner.shuckhorror.common.registry;

import dev.sterner.shuckhorror.common.block.*;
import dev.sterner.shuckhorror.common.item.CornItem;
import dev.sterner.shuckhorror.common.item.ScytheItem;
import dev.sterner.shuckhorror.common.item.SickleItem;
import dev.sterner.shuckhorror.common.util.Constants;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.PlantBlock;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.LinkedHashMap;
import java.util.Map;

public class SHObjects {
	public static final Map<Block, Identifier> BLOCKS = new LinkedHashMap<>();
	public static final Map<Item, Identifier> ITEMS = new LinkedHashMap<>();

	public static final Block CORN_BREAD = register("corn_bread", new CornBreadBlock(FabricBlockSettings.copyOf(Blocks.ROSE_BUSH)), gen(), true);

	public static final Block WILD_MAIZE = register("wild_maze", new PlantBlock(FabricBlockSettings.copyOf(Blocks.ROSE_BUSH)), gen(), false);
	public static final Block CORN_CROP = register("corn_crop", new CornCropBlock(FabricBlockSettings.copyOf(Blocks.ROSE_BUSH)), gen(), false);
	public static final Block CURSED_CORN_CROP = register("cursed_corn_crop", new CursedCornCropBlock(FabricBlockSettings.copyOf(Blocks.ROSE_BUSH)), gen(), false);
	public static final Block CANDY_CORN_CROP = register("candy_corn_crop", new CornCropBlock(FabricBlockSettings.copyOf(Blocks.ROSE_BUSH)), gen(), false);

	public static final Item CORN_COB_1 = register("corn_1", new CornItem(gen().food(Items.PORKCHOP.getFoodComponent()), 1, false));
	public static final Item CORN_COB_2 = register("corn_2", new CornItem(gen().food(Items.PORKCHOP.getFoodComponent()), 2, false));
	public static final Item CORN_COB_3 = register("corn_3", new CornItem(gen().food(Items.PORKCHOP.getFoodComponent()), 3, false));

	public static final Item CURSED_CORN_1 = register("cursed_corn_1", new CornItem(gen().food(Items.COOKED_CHICKEN.getFoodComponent()), 1, true));
	public static final Item CURSED_CORN_2 = register("cursed_corn_2", new CornItem(gen().food(Items.COOKED_CHICKEN.getFoodComponent()), 2, true));
	public static final Item CURSED_CORN_3 = register("cursed_corn_3", new CornItem(gen().food(Items.COOKED_CHICKEN.getFoodComponent()), 2, true));

	public static final Item ROASTED_CORN_1 = register("roasted_corn_1", new Item(gen().food(Items.COOKED_CHICKEN.getFoodComponent())));
	public static final Item ROASTED_CORN_2 = register("roasted_corn_2", new Item(gen().food(Items.COOKED_CHICKEN.getFoodComponent())));
	public static final Item ROASTED_CORN_3 = register("roasted_corn_3", new Item(gen().food(Items.COOKED_CHICKEN.getFoodComponent())));

	public static final Item CORN_KERNELS = register("corn_kernels", new AliasedBlockItem(CORN_CROP, gen()));
	public static final Item CURSED_CORN_KERNELS = register("cursed_corn_kernels", new AliasedBlockItem(CURSED_CORN_CROP, gen()));
	public static final Item POPCORN = register("popcorn", new Item(gen()
			.food(new FoodComponent.Builder()
					.hunger(2)
					.saturationModifier(0.1F)
					.statusEffect(new StatusEffectInstance(StatusEffects.SATURATION, 20 * 5, 1), 1.0F)
					.build()
			)));


	public static final Item CANDY_CORN = register("candy_corn", new Item(gen()
			.food(new FoodComponent.Builder()
					.hunger(2)
					.saturationModifier(0.1F)
					.statusEffect(new StatusEffectInstance(StatusEffects.SPEED, 20 * 5, 1), 1.0F)
					.alwaysEdible()
					.build()
			)));

	public static final Item GARMONBOZIA = register("garmonbozia", new Item(gen()
			.food(new FoodComponent.Builder()
					.hunger(10)
					.saturationModifier(0.6F)
					.build()
			)));
	public static final Item SICKLE = register("sickle", new SickleItem(gen()));
	public static final Item SCYTHE = register("scythe", new ScytheItem(gen()));





	private static Item.Settings gen() {
		return new Item.Settings().group(Constants.SHUCK_GROUP);
	}


	private static <T extends Item> T register(String name, T item) {
		ITEMS.put(item, Constants.id(name));
		return item;
	}

	private static <T extends Block> T register(String name, T block, Item.Settings settings, boolean createItem) {
		BLOCKS.put(block, Constants.id(name));
		if (createItem) {
			ITEMS.put(new BlockItem(block, settings), BLOCKS.get(block));
		}
		return block;
	}

	public static void init() {
		BLOCKS.keySet().forEach(block -> Registry.register(Registry.BLOCK, BLOCKS.get(block), block));
		ITEMS.keySet().forEach(item -> Registry.register(Registry.ITEM, ITEMS.get(item), item));
	}
}
