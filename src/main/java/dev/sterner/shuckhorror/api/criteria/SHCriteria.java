package dev.sterner.shuckhorror.api.criteria;

import dev.sterner.shuckhorror.data.criterion.BlockBreakCriteria;
import dev.sterner.shuckhorror.data.criterion.OnEntityTargetPlayerCriteria;
import net.minecraft.advancement.criterion.Criteria;

public class SHCriteria {
	public static final BlockBreakCriteria BLOCK_BREAK = Criteria.register(new BlockBreakCriteria());
	public static final OnEntityTargetPlayerCriteria ENTITY_TARGET_PLAYER = Criteria.register(new OnEntityTargetPlayerCriteria());

	public static void init() {
	}
}
