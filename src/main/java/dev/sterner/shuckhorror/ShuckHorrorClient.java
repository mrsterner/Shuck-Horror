package dev.sterner.shuckhorror;

import dev.sterner.shuckhorror.client.renderer.ScytheItemRenderer;
import dev.sterner.shuckhorror.common.registry.SHObjects;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;
import org.quiltmc.qsl.resource.loader.api.ResourceLoader;

public class ShuckHorrorClient implements ClientModInitializer {
	@Override
	public void onInitializeClient(ModContainer mod) {
		Identifier bigItemId = Registry.ITEM.getId(SHObjects.SCYTHE);
		ScytheItemRenderer scytheItemRenderer = new ScytheItemRenderer(bigItemId);
		ResourceLoader.get(ResourceType.CLIENT_RESOURCES).registerReloader(scytheItemRenderer);
		BuiltinItemRendererRegistry.INSTANCE.register(SHObjects.SCYTHE, scytheItemRenderer);
		ModelLoadingRegistry.INSTANCE.registerModelProvider((manager, out) -> {
			out.accept(new ModelIdentifier(bigItemId + "_gui", "inventory"));
			out.accept(new ModelIdentifier(bigItemId + "_handheld", "inventory"));
		});
	}
}
