package dev.sterner.shuckhorror.common.util;

import dev.sterner.shuckhorror.common.registry.SHObjects;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.block.Block;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Constants {
	public static final String MODID = "shuckhorror";
	public static final ItemGroup SHUCK_GROUP = FabricItemGroupBuilder.build(new Identifier(MODID, MODID), () -> new ItemStack(SHObjects.CORN_COB_3));

	public static Identifier id(String name) {
		return new Identifier(MODID, name);
	}

	public static class Tags {
		public static final TagKey<Block> CORN_COBLING_BLOCK_DESTROYABLE = TagKey.of(Registry.BLOCK_KEY, id("corn_coblin_blocks_destroyable"));
        public static final TagKey<Block> CORN = TagKey.of(Registry.BLOCK_KEY, id("corn"));;
    }

	public static class NBT {

		public static final String POSE_FLAGS = "pose_flag";
	}
}
