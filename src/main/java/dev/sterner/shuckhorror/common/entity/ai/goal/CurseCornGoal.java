package dev.sterner.shuckhorror.common.entity.ai.goal;

import dev.sterner.shuckhorror.client.network.packet.SpawnSoulParticlesPacket;
import dev.sterner.shuckhorror.common.registry.SHObjects;
import dev.sterner.shuckhorror.common.util.SHUtils;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.block.Block;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.WitchEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

import java.util.EnumSet;

public class CurseCornGoal extends Goal {
	private final WitchEntity mob;
	private final World world;
	protected int cooldown;
	private final int range;
	private final int maxYDifference;
	protected int lowestY;
	protected BlockPos targetPos = BlockPos.ORIGIN;

	public CurseCornGoal(WitchEntity mobEntity, int range, int maxYDifference) {
		this.mob = mobEntity;
		this.world = mobEntity.world;
		this.setControls(EnumSet.of(Control.LOOK, Control.MOVE));
		this.range = range;
		this.lowestY = 0;
		this.maxYDifference = maxYDifference;
	}

	@Override
	public boolean canStart() {
		if (this.cooldown > 0) {
			--this.cooldown;
			return false;
		} else {
			this.cooldown = 40;
			return this.findTargetPos();
		}
	}

	@Override
	public void start() {
		this.mob.getNavigation().stop();
	}


	@Override
	public void tick() {
		super.tick();
		if (this.world.getBlockState(targetPos).isOf(SHObjects.CORN_CROP)) {
			if (this.world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING)) {

				this.world.syncWorldEvent(2001, targetPos, Block.getRawIdFromState(SHObjects.CORN_CROP.getDefaultState()));
				SHUtils.transferBlockState(world, targetPos, SHObjects.CURSED_CORN_CROP.getDefaultState());

				if(world.getBlockState(targetPos.up()).isOf(SHObjects.CORN_CROP)){
					SHUtils.transferBlockState(world, targetPos.up(), SHObjects.CURSED_CORN_CROP.getDefaultState());
				}else if(world.getBlockState(targetPos.down()).isOf(SHObjects.CORN_CROP)){
					SHUtils.transferBlockState(world, targetPos.down(), SHObjects.CURSED_CORN_CROP.getDefaultState());
				}
				PlayerLookup.tracking(mob).forEach(tracking -> SpawnSoulParticlesPacket.send(tracking, targetPos));
			}
		}
	}

	@Override
	public boolean shouldContinue() {
		return this.isTargetPos(this.mob.world, this.targetPos);
	}

	protected boolean isTargetPos(WorldView world, BlockPos pos) {
		return world.getBlockState(pos).isOf(SHObjects.CORN_CROP);
	}

	protected boolean findTargetPos() {
		int i = this.range;
		int j = this.maxYDifference;
		BlockPos blockPos = this.mob.getBlockPos();
		BlockPos.Mutable mutable = new BlockPos.Mutable();

		for(int k = this.lowestY; k <= j; k = k > 0 ? -k : 1 - k) {
			for(int l = 0; l < i; ++l) {
				for(int m = 0; m <= l; m = m > 0 ? -m : 1 - m) {
					for(int n = m < l && m > -l ? l : 0; n <= l; n = n > 0 ? -n : 1 - n) {
						mutable.set(blockPos, m, k - 1, n);
						if (world.getBlockState(mutable).isOf(SHObjects.CORN_CROP)) {
							this.targetPos = mutable;
							return true;
						}
					}
				}
			}
		}

		return false;
	}
}
