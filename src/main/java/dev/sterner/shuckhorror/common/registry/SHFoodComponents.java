package dev.sterner.shuckhorror.common.registry;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.FoodComponent;

public class SHFoodComponents {
	public static final FoodComponent CANDY_CORN_COMPONENT =new FoodComponent.Builder()
			.hunger(2)
			.saturationModifier(0.1F)
			.statusEffect(new StatusEffectInstance(StatusEffects.SPEED, 20 * 5, 1), 1.0F)
			.alwaysEdible()
			.build();

	public static final FoodComponent GARMONBOZIA = new FoodComponent.Builder()
			.hunger(10)
			.saturationModifier(0.6F)
			.build();

	public static  final FoodComponent POPCORN = new FoodComponent.Builder()
			.hunger(2)
			.saturationModifier(0.1F)
			.statusEffect(new StatusEffectInstance(StatusEffects.SATURATION, 20 * 5, 1), 1.0F)
			.build();
}
