package dev.sterner.shuckhorror.common.item;

import dev.sterner.shuckhorror.client.network.packet.SpawnSoulParticlesPacket;
import dev.sterner.shuckhorror.common.registry.SHObjects;
import dev.sterner.shuckhorror.common.util.SHUtils;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;

public class SHDebugStick extends Item {
	public SHDebugStick(Settings settings) {
		super(settings);
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		if (context.getWorld().getBlockState(context.getBlockPos()).isOf(SHObjects.CORN_CROP)) {
			SHUtils.transferBlockState(context.getWorld(), context.getBlockPos(), SHObjects.CURSED_CORN_CROP.getDefaultState());
		}
		if (context.getWorld().getBlockState(context.getBlockPos().up()).isOf(SHObjects.CORN_CROP)) {
			SHUtils.transferBlockState(context.getWorld(), context.getBlockPos().up(), SHObjects.CURSED_CORN_CROP.getDefaultState());
		}
		if (context.getWorld().getBlockState(context.getBlockPos().down()).isOf(SHObjects.CORN_CROP)) {
			SHUtils.transferBlockState(context.getWorld(), context.getBlockPos().down(), SHObjects.CURSED_CORN_CROP.getDefaultState());
		}
		if(context.getPlayer() instanceof ServerPlayerEntity serverPlayerEntity){
			SpawnSoulParticlesPacket.send(serverPlayerEntity, context.getBlockPos());
		}

		return super.useOnBlock(context);
	}
}
