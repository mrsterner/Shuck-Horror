package dev.sterner.shuckhorror.common.item;

import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterials;

public class SickleItem extends SwordItem {
	public SickleItem(Settings settings) {
		super(ToolMaterials.IRON, 3, -2.4F, settings);
	}
}
