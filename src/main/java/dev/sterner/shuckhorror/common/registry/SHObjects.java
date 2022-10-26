package dev.sterner.shuckhorror.common.registry;

import dev.sterner.shuckhorror.common.block.*;
import dev.sterner.shuckhorror.common.item.CornItem;
import dev.sterner.shuckhorror.common.item.ScytheItem;
import dev.sterner.shuckhorror.common.item.SickleItem;
import dev.sterner.shuckhorror.common.util.Constants;
import net.minecraft.block.*;
import net.minecraft.item.*;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.LinkedHashMap;
import java.util.Map;

public class SHObjects {
	public static final Map<Block, Identifier> BLOCKS = new LinkedHashMap<>();
	public static final Map<Item, Identifier> ITEMS = new LinkedHashMap<>();

	//Settings
	public static final AbstractBlock.Settings CORN = AbstractBlock.Settings.of(Material.REPLACEABLE_PLANT).noCollision().breakInstantly().sounds(BlockSoundGroup.GRASS).offsetType(AbstractBlock.OffsetType.XZ);

	//Blocks
	public static final Block WILD_MAIZE = register("wild_maize", new PlantBlock(CORN), genSettings(), false);
	public static final Block CORN_CROP = register("corn_crop", new CornCropBlock(CORN), genSettings(), false);
	public static final Block CURSED_CORN_CROP = register("cursed_corn_crop", new CursedCornCropBlock(CORN), genSettings(), false);
	public static final Block CANDY_CORN_CROP = register("candy_corn_crop", new CornCropBlock(CORN), genSettings(), false);

	public static final Block CORN_BREAD_BLOCK = register("corn_bread", new CornBreadBlock(CORN), genSettings(), false);

	//Items
	public static final Item CORN_BREAD = register("corn_bread", new AliasedBlockItem(CORN_BREAD_BLOCK, genSettings()));

	public static final Item CORN_COB_1 = register("corn_1", new CornItem(genSettings().food(Items.PORKCHOP.getFoodComponent()), 1, false));
	public static final Item CORN_COB_2 = register("corn_2", new CornItem(genSettings().food(Items.PORKCHOP.getFoodComponent()), 2, false));
	public static final Item CORN_COB_3 = register("corn_3", new CornItem(genSettings().food(Items.PORKCHOP.getFoodComponent()), 3, false));

	public static final Item CURSED_CORN_1 = register("cursed_corn_1", new CornItem(genSettings().food(Items.COOKED_CHICKEN.getFoodComponent()), 1, true));
	public static final Item CURSED_CORN_2 = register("cursed_corn_2", new CornItem(genSettings().food(Items.COOKED_CHICKEN.getFoodComponent()), 2, true));
	public static final Item CURSED_CORN_3 = register("cursed_corn_3", new CornItem(genSettings().food(Items.COOKED_CHICKEN.getFoodComponent()), 2, true));

	public static final Item ROASTED_CORN_1 = register("roasted_corn_1", new Item(genSettings().food(Items.COOKED_CHICKEN.getFoodComponent())));
	public static final Item ROASTED_CORN_2 = register("roasted_corn_2", new Item(genSettings().food(Items.COOKED_CHICKEN.getFoodComponent())));
	public static final Item ROASTED_CORN_3 = register("roasted_corn_3", new Item(genSettings().food(Items.COOKED_CHICKEN.getFoodComponent())));

	public static final Item CORN_KERNELS = register("corn_kernels", new AliasedBlockItem(CORN_CROP, genSettings()));
	public static final Item CURSED_CORN_KERNELS = register("cursed_corn_kernels", new AliasedBlockItem(CURSED_CORN_CROP, genSettings()));
	public static final Item POPCORN = register("popcorn", new Item(genSettings().food(SHFoodComponents.POPCORN)));
	public static final Item CANDY_CORN = register("candy_corn", new Item(genSettings().food(SHFoodComponents.CANDY_CORN_COMPONENT)));
	public static final Item GARMONBOZIA = register("garmonbozia", new Item(genSettings().food(SHFoodComponents.GARMONBOZIA)));

	public static final Item SICKLE = register("sickle", new SickleItem(genSettings()));
	public static final Item SCYTHE = register("scythe", new ScytheItem(genSettings()));





	private static Item.Settings genSettings() {
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
