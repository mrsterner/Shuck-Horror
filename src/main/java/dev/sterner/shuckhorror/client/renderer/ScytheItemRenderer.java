package dev.sterner.shuckhorror.client.renderer;

import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.Unit;
import net.minecraft.util.profiler.Profiler;
import org.jetbrains.annotations.NotNull;
import org.quiltmc.qsl.resource.loader.api.reloader.IdentifiableResourceReloader;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class ScytheItemRenderer implements BuiltinItemRendererRegistry.DynamicItemRenderer, IdentifiableResourceReloader {
	private final Identifier id;
	private final Identifier itemId;
	private ItemRenderer itemRenderer;
	private BakedModel inventoryScytheModel;
	private BakedModel worldScytheModel;

	public ScytheItemRenderer(Identifier id) {
		this.id = new Identifier(id + "_renderer");
		this.itemId = id;
	}

	@Override
	public void render(ItemStack stack, ModelTransformation.Mode mode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		matrices.pop();
		matrices.push();
		if (mode != ModelTransformation.Mode.FIRST_PERSON_LEFT_HAND && mode != ModelTransformation.Mode.FIRST_PERSON_RIGHT_HAND && mode != ModelTransformation.Mode.THIRD_PERSON_LEFT_HAND && mode != ModelTransformation.Mode.THIRD_PERSON_RIGHT_HAND && mode != ModelTransformation.Mode.NONE) {
			itemRenderer.renderItem(stack, mode, false, matrices, vertexConsumers, light, overlay, this.inventoryScytheModel);
		} else {
			boolean leftHanded;
			switch (mode) {
				case FIRST_PERSON_LEFT_HAND, THIRD_PERSON_LEFT_HAND -> leftHanded = true;
				default -> leftHanded = false;
			}
			itemRenderer.renderItem(stack, mode, leftHanded, matrices, vertexConsumers, light, overlay, this.worldScytheModel);
		}
	}

	@Override
	public @NotNull Identifier getQuiltId() {
		return this.id;
	}

	@Override
	public CompletableFuture<Void> reload(Synchronizer synchronizer, ResourceManager manager, Profiler prepareProfiler, Profiler applyProfiler, Executor prepareExecutor, Executor applyExecutor) {
		return synchronizer.whenPrepared(Unit.INSTANCE).thenRunAsync(() -> {
			applyProfiler.startTick();
			applyProfiler.push("listener");
			final MinecraftClient client = MinecraftClient.getInstance();
			this.itemRenderer = client.getItemRenderer();
			this.inventoryScytheModel = client.getBakedModelManager().getModel(new ModelIdentifier(this.itemId + "_gui", "inventory"));
			this.worldScytheModel = client.getBakedModelManager().getModel(new ModelIdentifier(this.itemId + "_handheld", "inventory"));
			applyProfiler.pop();
			applyProfiler.endTick();
		}, applyExecutor);
	}
}
