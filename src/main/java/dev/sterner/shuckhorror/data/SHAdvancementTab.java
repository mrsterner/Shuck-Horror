package dev.sterner.shuckhorror.data;

import dev.sterner.shuckhorror.common.registry.SHEntityTypes;
import dev.sterner.shuckhorror.common.registry.SHObjects;
import dev.sterner.shuckhorror.common.util.Constants;
import dev.sterner.shuckhorror.data.criterion.BlockBreakCriteria;
import dev.sterner.shuckhorror.data.criterion.HitEntityWithItemCriteria;
import dev.sterner.shuckhorror.data.criterion.OnEntityTargetPlayerCriteria;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementDisplay;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.advancement.AdvancementRewards;
import net.minecraft.advancement.criterion.ConsumeItemCriterion;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.advancement.criterion.OnKilledCriterion;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.predicate.entity.DamageSourcePredicate;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.function.Consumer;

public class SHAdvancementTab implements Consumer<Consumer<Advancement>> {
	private static final Item[] FOOD_ITEMS = new Item[]{
			SHObjects.CURSED_CORN_1,
			SHObjects.ROASTED_CORN_1,
			SHObjects.CORN_BREAD
	};


	public static AdvancementDisplay makeDisplay(AdvancementFrame frame, ItemConvertible item, String titleKey) {
		return new AdvancementDisplay(item.asItem().getDefaultStack(),
				Text.translatable(Constants.MODID + ".advancement." + titleKey),
				Text.translatable(Constants.MODID + ".advancement." + titleKey + ".desc"),
				new Identifier("textures/gui/advancements/backgrounds/adventure.png"),
				frame,
				true,
				true,
				false
		);
	}

	@Override
	public void accept(Consumer<Advancement> advancementConsumer) {
		Advancement root = Advancement.Task.create()
				.display(makeDisplay(AdvancementFrame.TASK, SHObjects.CORN_KERNELS, "root"))
				.criterion("inventory_changed", InventoryChangedCriterion.Conditions.items(SHObjects.CORN_KERNELS))
				.build(advancementConsumer, "shuckhorror:shuckhorror/root");


		Advancement encounterCuredCornCrop = Advancement.Task.create()
				.display(makeDisplay(AdvancementFrame.TASK, SHObjects.CURSED_CORN_1, "cursed_corn"))
				.criterion("destroy_cursed_corn", BlockBreakCriteria.Conditions.create(SHObjects.CURSED_CORN_CROP))
				.parent(root)
				.build(advancementConsumer, "shuckhorror:shuckhorror/cursed_corn");

		Advancement encounterCornCoblin = Advancement.Task.create()
				.display(makeDisplay(AdvancementFrame.TASK, SHObjects.POPCORN, "corn_coblin"))
				.criterion("encounter_corn_coblin", OnEntityTargetPlayerCriteria.Conditions.create(EntityPredicate.Extended.EMPTY, EntityPredicate.Extended.ofLegacy(EntityPredicate.Builder.create().type(SHEntityTypes.CORN_COBLIN).build())))
				.parent(root)
				.build(advancementConsumer, "shuckhorror:shuckhorror/corn_coblin");

		Advancement encounterChildOfTheCorn = Advancement.Task.create()
				.display(makeDisplay(AdvancementFrame.TASK, SHObjects.CURSED_CORN_1, "child_of_the_corn"))
				.criterion("encounter_child_of_the_corn", new OnEntityTargetPlayerCriteria.Conditions(EntityPredicate.Extended.EMPTY, EntityPredicate.Extended.ofLegacy(EntityPredicate.Builder.create().type(SHEntityTypes.CHILD_OF_THE_CORN).build())))
				.parent(root)
				.build(advancementConsumer, "shuckhorror:shuckhorror/child_of_the_corn");

		Advancement killChildOfTheCorn = Advancement.Task.create()
				.display(makeDisplay(AdvancementFrame.TASK, SHObjects.SICKLE, "you_shall_also_reap"))
				.criterion("on_killed", OnKilledCriterion.Conditions.createPlayerKilledEntity(EntityPredicate.Builder.create().type(SHEntityTypes.CHILD_OF_THE_CORN)))
				.parent(encounterChildOfTheCorn)
				.build(advancementConsumer, "shuckhorror:shuckhorror/you_shall_also_reap");

		Advancement strikeEntityWithSickle = Advancement.Task.create()
				.display(makeDisplay(AdvancementFrame.GOAL, Items.SKELETON_SKULL, "grim_harvest"))
				.criterion("strike_entity_with_sickle", new HitEntityWithItemCriteria.Conditions(EntityPredicate.Extended.EMPTY, ItemPredicate.Builder.create().items(SHObjects.SICKLE).build(), DamageSourcePredicate.EMPTY, EntityPredicate.Extended.EMPTY))
				.parent(killChildOfTheCorn)
				.build(advancementConsumer, "shuckhorror:shuckhorror/grim_harvest");

		Advancement candyCorn = Advancement.Task.create()
				.display(makeDisplay(AdvancementFrame.TASK, SHObjects.CANDY_CORN, "obtain"))
				.criterion("inventory_changed", InventoryChangedCriterion.Conditions.items(SHObjects.CANDY_CORN))
				.parent(encounterCuredCornCrop)
				.build(advancementConsumer, "shuckhorror:shuckhorror/halloween_classic");

		this.requireFoodItemsEaten(
				Advancement.Task.create())
				.parent(root)
				.display(makeDisplay(AdvancementFrame.CHALLENGE, SHObjects.CORN_BREAD, "cornn_flaek"))
				.rewards(AdvancementRewards.Builder.experience(100))
				.build(advancementConsumer, "shuckhorror:shuckhorror/cornn_flaek");

	}

	private Advancement.Task requireFoodItemsEaten(Advancement.Task builder) {
		for (Item item : FOOD_ITEMS) {
			builder.criterion(Registry.ITEM.getId(item).getPath(), ConsumeItemCriterion.Conditions.item(item));
		}
		return builder;
	}
}
