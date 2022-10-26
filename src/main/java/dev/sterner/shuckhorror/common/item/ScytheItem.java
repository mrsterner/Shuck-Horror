package dev.sterner.shuckhorror.common.item;

import net.minecraft.item.Item;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterials;

public class ScytheItem extends SwordItem {
	public ScytheItem(Settings settings) {
		super(ToolMaterials.NETHERITE, 4, -2.7F, settings.fireproof());
	}


}
