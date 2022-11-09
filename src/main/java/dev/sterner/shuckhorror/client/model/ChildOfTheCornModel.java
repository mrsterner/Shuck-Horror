package dev.sterner.shuckhorror.client.model;

import dev.sterner.shuckhorror.ShuckHorror;
import dev.sterner.shuckhorror.common.entity.ChildOfTheCornEntity;
import dev.sterner.shuckhorror.common.util.Constants;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class ChildOfTheCornModel <T extends ChildOfTheCornEntity> extends BipedEntityModel<T> {

	public static final EntityModelLayer LAYER_LOCATION = new EntityModelLayer(new Identifier(Constants.MODID, "ghost_farmer"), "main");
	private final ModelPart rightArm;
	private final ModelPart leftArm;
	private final ModelPart head;
	private final ModelPart body;

	public ChildOfTheCornModel(ModelPart root) {
		super(root);
		this.head = root.getChild(EntityModelPartNames.HEAD);
		this.body = root.getChild(EntityModelPartNames.BODY);
		this.rightArm = root.getChild(EntityModelPartNames.RIGHT_ARM);
		this.leftArm = root.getChild(EntityModelPartNames.LEFT_ARM);
	}

	public static TexturedModelData createBodyLayer() {
		ModelData meshdefinition = BipedEntityModel.getModelData(new Dilation(0), 0);
		ModelPartData partdefinition = meshdefinition.getRoot();

		ModelPartData rightArm = partdefinition.addChild(EntityModelPartNames.RIGHT_ARM, ModelPartBuilder.create().uv(40, 46).cuboid(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(-5.0F, 2.0F, 0.0F, 0.0F, 0.0F, 0.0873F));
		ModelPartData rCloak = rightArm.addChild("rCloak", ModelPartBuilder.create().uv(88, 15).mirrored().cuboid(-1.0F, -2.75F, -1.5F, 17.0F, 11.0F, 3.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(1.0F, 1.0F, 0.0F));
		ModelPartData leftArm = partdefinition.addChild(EntityModelPartNames.LEFT_ARM, ModelPartBuilder.create().uv(40, 46).mirrored().cuboid(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(5.0F, 2.0F, 0.0F, 0.0F, 0.0F, -0.0873F));
		ModelPartData lCloak = leftArm.addChild("lCloak", ModelPartBuilder.create().uv(88, 15).cuboid(-16.0F, -2.75F, -1.5F, 17.0F, 11.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(-1.0F, 1.0F, 0.0F));
		ModelPartData head = partdefinition.addChild(EntityModelPartNames.HEAD, ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		ModelPartData nose = head.addChild("nose", ModelPartBuilder.create().uv(26, 0).cuboid(-1.0F, -1.0F, -6.0F, 2.0F, 4.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -2.0F, 0.0F));
		ModelPartData hat = head.addChild("hat", ModelPartBuilder.create().uv(87, 0).cuboid(-6.0F, -2.25F, -4.0F, 12.0F, 3.0F, 8.0F, new Dilation(0.01F)), ModelTransform.pivot(0.0F, -7.0F, 0.0F));
		ModelPartData threads = hat.addChild("threads", ModelPartBuilder.create().uv(49, 13).cuboid(-4.5F, -6.0F, -4.5F, 9.0F, 15.0F, 9.0F, new Dilation(0.01F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		ModelPartData body = partdefinition.addChild(EntityModelPartNames.BODY, ModelPartBuilder.create().uv(0, 18).cuboid(-4.0F, 0.0F, -3.0F, 8.0F, 14.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		ModelPartData robe = body.addChild("robe", ModelPartBuilder.create().uv(0, 38).cuboid(-4.0F, 0.0F, -3.0F, 8.0F, 20.0F, 6.0F, new Dilation(0.5F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		ModelPartData tail01 = body.addChild("tail01", ModelPartBuilder.create().uv(60, 39).cuboid(-3.5F, -0.5F, -2.5F, 7.0F, 8.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 13.5F, 0.0F, 0.2182F, 0.0F, 0.0F));
		ModelPartData tail02a = tail01.addChild("tail02a", ModelPartBuilder.create().uv(90, 33).cuboid(-3.5F, -6.0F, 0.0F, 7.0F, 14.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 7.5F, 0.0F, 0.0F, -0.6109F, 0.0F));
		ModelPartData tail02b = tail01.addChild("tail02b", ModelPartBuilder.create().uv(90, 33).cuboid(-3.5F, -6.0F, 0.0F, 7.0F, 14.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 7.5F, 0.0F, 0.0F, 0.6109F, 0.0F));

		return TexturedModelData.of(meshdefinition, 128, 64);
	}



	@Override
	public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		super.setAngles(entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
		this.rightLeg.pitch = 0.0f;
		this.leftLeg.pitch = 0.0f;
		this.rightLeg.yaw = 0.0f;
		this.leftLeg.yaw = 0.0f;
		this.rightLeg.roll = 0.0f;
		this.leftLeg.roll = 0.0f;

		this.rightArm.pitch = 0.0f;
		this.leftArm.pitch = 0.0f;

		this.rightArm.roll = 0.1f + MathHelper.sin(animationProgress / 20) / 10;
		this.leftArm.roll = -rightArm.roll;



	}



	@Override
	public void setArmAngle(Arm arm, MatrixStack matrices) {
		ModelPart modelPart = this.getArm(arm);
		modelPart.rotate(matrices);
	}

	@Override
	public void setVisible(boolean visible) {
		this.head.visible = visible;
		this.hat.visible = visible;
		this.body.visible = visible;
		this.rightArm.visible = visible;
		this.leftArm.visible = visible;
		this.rightLeg.visible = false;
		this.leftLeg.visible = false;
	}

	private void copyRotation(ModelPart to, ModelPart from) {
		to.pitch = from.pitch;
		to.yaw = from.yaw;
		to.roll = from.roll;
	}
}
