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
import net.minecraft.advancement.criterion.CriterionConditions;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.advancement.criterion.OnKilledCriterion;
import net.minecraft.block.Blocks;
import net.minecraft.data.server.EndTabAdvancementGenerator;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.predicate.entity.DamageSourcePredicate;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class SHAdvancementTab implements Consumer<Consumer<Advancement>> {
	static AdvancementDisplay makeDisplay(ItemConvertible item, String titleKey) {
		return new AdvancementDisplay(item.asItem().getDefaultStack(),
				Text.translatable(Constants.MODID + ".advancement." + titleKey),
				Text.translatable(Constants.MODID + ".advancement." + titleKey + ".desc"),
				new Identifier("textures/gui/advancements/backgrounds/adventure.png"),
				AdvancementFrame.TASK,
				true,
				true,
				false
		);
	}

	@Override
	public void accept(Consumer<Advancement> advancementConsumer) {
		Advancement root = Advancement.Builder.create()
				.display(makeDisplay(SHObjects.CORN_KERNELS, "root"))
				.criterion("inventory_changed", InventoryChangedCriterion.Conditions.items(SHObjects.CORN_KERNELS))
				.build(advancementConsumer, "shuckhorror:shuckhorror/root");


		Advancement encounterCuredCornCrop = Advancement.Builder.create()
				.display(makeDisplay(SHObjects.CURSED_CORN_1, "cursed_corn"))
				.criterion("destroy_cursed_corn", new BlockBreakCriteria.Conditions(EntityPredicate.Extended.EMPTY, SHObjects.CURSED_CORN_CROP))
				.parent(root)
				.build(advancementConsumer, "shuckhorror:shuckhorror/cursed_corn");

		Advancement encounterCornCoblin = Advancement.Builder.create()
				.display(makeDisplay(SHObjects.POPCORN, "corn_coblin"))
				.criterion("encounter_corn_coblin", new OnEntityTargetPlayerCriteria.Conditions(EntityPredicate.Extended.EMPTY, SHEntityTypes.CORN_COBLIN))
				.parent(root)
				.build(advancementConsumer, "shuckhorror:shuckhorror/corn_coblin");

		Advancement encounterChildOfTheCorn = Advancement.Builder.create()
				.display(makeDisplay(SHObjects.CURSED_CORN_1, "child_of_the_corn"))
				.criterion("encounter_child_of_the_corn", new OnEntityTargetPlayerCriteria.Conditions(EntityPredicate.Extended.EMPTY, SHEntityTypes.CHILD_OF_THE_CORN))
				.parent(root)
				.build(advancementConsumer, "shuckhorror:shuckhorror/child_of_the_corn");

		Advancement killChildOfTheCorn = Advancement.Builder.create()
				.display(makeDisplay(SHObjects.SICKLE, "you_shall_also_reap"))
				.criterion("on_killed", OnKilledCriterion.Conditions.createPlayerKilledEntity(EntityPredicate.Builder.create().type(SHEntityTypes.CHILD_OF_THE_CORN)))
				.parent(encounterChildOfTheCorn)
				.build(advancementConsumer, "shuckhorror:shuckhorror/you_shall_also_reap");

		Advancement stickEntityWithSickle = Advancement.Builder.create()
				.display(makeDisplay(Items.SKELETON_SKULL, "grim_harvest"))
				.criterion("strike_entity_with_sickle", new HitEntityWithItemCriteria.Conditions(EntityPredicate.Extended.EMPTY, SHObjects.SICKLE.getDefaultStack(), DamageSourcePredicate.EMPTY, EntityPredicate.Extended.EMPTY))
				.parent(encounterChildOfTheCorn)
				.build(advancementConsumer, "shuckhorror:shuckhorror/grim_harvest");
		Advancement candyCorn = Advancement.Builder.create()
				.display(makeDisplay(SHObjects.CANDY_CORN, "obtain"))
				.criterion("inventory_changed", InventoryChangedCriterion.Conditions.items(SHObjects.CANDY_CORN))
				.parent(encounterCuredCornCrop)
				.build(advancementConsumer, "shuckhorror:shuckhorror/halloween_classic");


	}
}
