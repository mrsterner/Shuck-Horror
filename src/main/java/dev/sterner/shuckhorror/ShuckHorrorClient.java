package dev.sterner.shuckhorror;

import dev.sterner.shuckhorror.client.model.ChildOfTheCornModel;
import dev.sterner.shuckhorror.client.network.packet.SpawnSoulParticlesPacket;
import dev.sterner.shuckhorror.client.renderer.ChildOfTheCornRenderer;
import dev.sterner.shuckhorror.client.renderer.ScytheItemRenderer;
import dev.sterner.shuckhorror.common.registry.SHEntityTypes;
import dev.sterner.shuckhorror.common.registry.SHObjects;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ShuckHorrorClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(),SHObjects.CORN_CROP, SHObjects.CURSED_CORN_CROP, SHObjects.CANDY_CORN_CROP, SHObjects.WILD_MAIZE);
		ClientPlayNetworking.registerGlobalReceiver(SpawnSoulParticlesPacket.ID, SpawnSoulParticlesPacket::handle);
		EntityModelLayerRegistry.registerModelLayer(ChildOfTheCornModel.LAYER_LOCATION, ChildOfTheCornModel::createBodyLayer);
		EntityRendererRegistry.register(SHEntityTypes.CHILD_OF_THE_CORN, ChildOfTheCornRenderer::new);

				Identifier bigItemId = Registry.ITEM.getId(SHObjects.SCYTHE);
		ScytheItemRenderer scytheItemRenderer = new ScytheItemRenderer(bigItemId);
		ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(scytheItemRenderer);
		BuiltinItemRendererRegistry.INSTANCE.register(SHObjects.SCYTHE, scytheItemRenderer);
		ModelLoadingRegistry.INSTANCE.registerModelProvider((manager, out) -> {
			out.accept(new ModelIdentifier(bigItemId + "_gui", "inventory"));
			out.accept(new ModelIdentifier(bigItemId + "_handheld", "inventory"));
		});
	}
}
