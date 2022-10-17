package dev.sterner.shuckhorror;

import dev.sterner.shuckhorror.client.renderer.ScytheItemRenderer;
import dev.sterner.shuckhorror.common.registry.SHObjects;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ShuckHorrorClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(),SHObjects.CORN_CROP, SHObjects.CURSED_CORN_CROP, SHObjects.CANDY_CORN_CROP);


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
