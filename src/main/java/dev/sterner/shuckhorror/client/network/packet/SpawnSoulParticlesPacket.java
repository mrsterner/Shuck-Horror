package dev.sterner.shuckhorror.client.network.packet;

import dev.sterner.shuckhorror.common.util.Constants;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class SpawnSoulParticlesPacket {
	public static final Identifier ID = Constants.id("soul_particles");

	public static void send(PlayerEntity player, BlockPos blockPos) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeBlockPos(blockPos);
		ServerPlayNetworking.send((ServerPlayerEntity) player, ID, buf);
	}

	public static void handle(MinecraftClient client, ClientPlayNetworkHandler network, PacketByteBuf buf, PacketSender sender) {
		BlockPos blockPos = buf.readBlockPos();
		client.execute(() -> {
			ClientWorld world = client.world;
			if (world != null) {
				if (blockPos != null) {
					for (int i = 0; i < 32; i++) {
						world.addParticle(ParticleTypes.SOUL, blockPos.getX() + world.getRandom().nextDouble(), blockPos.getY() + world.getRandom().nextDouble(), blockPos.getZ() + world.getRandom().nextDouble(), 0, 0, 0);
					}
				}
			}
		});
	}
}
