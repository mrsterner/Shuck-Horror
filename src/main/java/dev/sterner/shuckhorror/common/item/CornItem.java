package dev.sterner.shuckhorror.common.item;

import dev.sterner.shuckhorror.common.registry.SHObjects;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

public class  CornItem extends Item {
	private final int variant;
	private final boolean cursed;
	private static final int MAX_USE_TIME = 40;

	public CornItem(Settings settings, int variant, boolean cursed) {
		super(settings);
		this.variant = variant;
		this.cursed = cursed;
	}

	private ItemStack getNextEatingStack(int variant, boolean cursed){
		return new ItemStack(switch (variant) {
			case 2 -> cursed ? SHObjects.CURSED_CORN_1: SHObjects.CORN_COB_1;
			case 3 -> cursed ? SHObjects.CURSED_CORN_2: SHObjects.CORN_COB_2;
			default -> ItemStack.EMPTY.getItem();
		});
	}

	@Override
	public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
		super.finishUsing(stack, world, user);
		if (user instanceof ServerPlayerEntity serverPlayerEntity) {
			Criteria.CONSUME_ITEM.trigger(serverPlayerEntity, stack);
			serverPlayerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
		}

		if(cursed){
			//TODO cursed stuff
		}

		if (user instanceof PlayerEntity playerEntity && !playerEntity.getAbilities().creativeMode) {
			ItemStack corn = getNextEatingStack(variant, cursed);
			if (!playerEntity.getInventory().insertStack(corn)) {
				playerEntity.dropItem(corn, false);
			}
		}

		return stack;
	}

	@Override
	public int getMaxUseTime(ItemStack stack) {
		return MAX_USE_TIME;
	}

	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.EAT;
	}

	@Override
	public SoundEvent getDrinkSound() {
		return SoundEvents.ENTITY_GENERIC_EAT;
	}

	@Override
	public SoundEvent getEatSound() {
		return SoundEvents.ENTITY_GENERIC_EAT;
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		return ItemUsage.consumeHeldItem(world, user, hand);
	}

}
