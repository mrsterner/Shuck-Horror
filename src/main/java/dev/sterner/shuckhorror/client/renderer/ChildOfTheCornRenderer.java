package dev.sterner.shuckhorror.client.renderer;

import dev.sterner.shuckhorror.client.model.ChildOfTheCornModel;
import dev.sterner.shuckhorror.common.entity.ChildOfTheCornEntity;
import dev.sterner.shuckhorror.common.util.Constants;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class ChildOfTheCornRenderer extends MobEntityRenderer<ChildOfTheCornEntity, ChildOfTheCornModel<ChildOfTheCornEntity>> {
	public static final Identifier EMPTY = new Identifier("minecraft", "textures/block/redstone_dust_overlay.png");
	private static Identifier[] TEXTURES;

	public ChildOfTheCornRenderer(EntityRendererFactory.Context context) {
		super(context, new ChildOfTheCornModel<>(context.getPart(ChildOfTheCornModel.LAYER_LOCATION)), 0.5f);
		this.addFeature(new HeldItemFeatureRenderer<>(this, context.getHeldItemRenderer()));
		this.addFeature(new FeatureRenderer<>(this) {
			@Override
			public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, ChildOfTheCornEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
				if(TEXTURES == null){
					int variants = entity.getVariants();
					TEXTURES = new Identifier[variants];
					for (int i = 0; i < variants; i++) {
						TEXTURES[i] = Constants.id("textures/entity/ghost_farmer_" + i + ".png");
					}
				}
				VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(TEXTURES[entity.getDataTracker().get(ChildOfTheCornEntity.VARIANT)]));
				getContextModel().render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1, 1, 1, 0.5F);
			}
		});
	}



	@Override
	public Identifier getTexture(ChildOfTheCornEntity entity) {
		return EMPTY;
	}
}
