package dev.sterner.shuckhorror.common.registry;

import dev.sterner.shuckhorror.common.block.*;
import dev.sterner.shuckhorror.common.util.Constants;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.quiltmc.qsl.block.extensions.api.QuiltBlockSettings;

import java.util.LinkedHashMap;
import java.util.Map;

public class SHObjects {
	public static final Map<Block, Identifier> BLOCKS = new LinkedHashMap<>();
	public static final Map<Item, Identifier> ITEMS = new LinkedHashMap<>();

	public static final Item CORN_COB = register("corn_cob", new Item(gen()));
	public static final Item CORN_KERNELS = register("corn_kernels", new Item(gen()));
	public static final Item CURSED_CORN_KERNELS = register("cursed_corn_kernels", new Item(gen()));
	public static final Item POPCORN = register("popcorn", new Item(gen()));
	public static final Item ROASTED_CORN = register("roasted_corn", new Item(gen()));
	public static final Item CURSED_CORN_COB = register("cursed_corn_cob", new Item(gen()));
	public static final Item CANDY_CORN = register("candy_corn", new Item(gen()));
	public static final Item CREAM_CORN = register("cream_corn", new Item(gen()));
	public static final Item BOWL_OF_CREAM_CORN = register("bowl_of_cream_corn", new Item(gen()));
	public static final Item SICKLE = register("sickle", new Item(gen()));
	public static final Item SCYTHE = register("scythe", new Item(gen()));

	public static final Block CORN_BREAD = register("corn_bread", new CornBreadBlock(QuiltBlockSettings.copyOf(Blocks.ROSE_BUSH)), gen(), true);

	public static final Block WILD_MAIZE = register("wild_maze", new TallCropBlock(QuiltBlockSettings.copyOf(Blocks.ROSE_BUSH)), gen(), true);
	public static final Block CORN_CROP = register("corn_crop", new CornCropBlock(QuiltBlockSettings.copyOf(Blocks.ROSE_BUSH)), gen(), true);
	public static final Block CURSED_CORN_CROP = register("cursed_corn_crop", new CursedCornCropBlock(QuiltBlockSettings.copyOf(Blocks.ROSE_BUSH)), gen(), true);
	public static final Block CANDY_CORN_CROP = register("candy_corn_block", new CandyCornCropBlock(QuiltBlockSettings.copyOf(Blocks.ROSE_BUSH)), gen(), true);



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
