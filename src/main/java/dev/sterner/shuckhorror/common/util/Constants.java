package dev.sterner.shuckhorror.common.util;

import dev.sterner.shuckhorror.common.registry.SHObjects;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.quiltmc.qsl.item.group.api.QuiltItemGroup;

public class Constants {
	public static final String MODID = "shuckhorror";
	public static final QuiltItemGroup SHUCK_GROUP = QuiltItemGroup.builder(Constants.id("items")).icon(() -> new ItemStack(SHObjects.CORN_COB)).build();

	public static Identifier id(String name) {
		return new Identifier(MODID, name);
	}

	public static class Tags {
		public static final TagKey<Block> CORN_COBLING_BLOCK_DESTROYABLE = TagKey.of(Registry.BLOCK_KEY, id("corn_coblin_blocks_destroyable"));
	}
}
