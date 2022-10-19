package dev.sterner.shuckhorror.data;

import dev.sterner.shuckhorror.common.registry.SHEntityTypes;
import dev.sterner.shuckhorror.common.registry.SHObjects;
import dev.sterner.shuckhorror.common.util.Constants;
import dev.sterner.shuckhorror.data.criterion.BlockBreakCriteria;
import dev.sterner.shuckhorror.data.criterion.CoblinTrackedCriterion;
import dev.sterner.shuckhorror.data.criterion.OnEntityTargetPlayerCriteria;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementDisplay;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.advancement.criterion.OnKilledCriterion;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemConvertible;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class SHAdvancementTab implements Consumer<Consumer<Advancement>> {
	static AdvancementDisplay makeDisplay(ItemConvertible item, String titleKey) {
		return new AdvancementDisplay(item.asItem().getDefaultStack(),
				Text.translatable(Constants.MODID + ".advancement." + titleKey),
				Text.translatable(Constants.MODID + ".advancement." + titleKey + ".desc"),
				new Identifier(Constants.MODID + "textures/item/corn_cob_1.png"),
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
				.build(advancementConsumer, "shuckhorror:root");


		Advancement encounterCuredCornCrop = Advancement.Builder.create()
				.display(makeDisplay(SHObjects.CURSED_CORN_1, "cursed_corn"))
				.criterion("destroy_cursed_corn", new BlockBreakCriteria.Conditions(EntityPredicate.Extended.EMPTY, SHObjects.CURSED_CORN_CROP))
				.build(advancementConsumer, Constants.MODID + ":" + "cursed_corn");

		Advancement encounterCornCoblin = Advancement.Builder.create()
				.display(makeDisplay(SHObjects.POPCORN, "corn_coblin"))
				.criterion("encounter_corn_coblin", OnKilledCriterion.Conditions.createPlayerKilledEntity(EntityPredicate.Builder.create().type(SHEntityTypes.CORN_COBLIN)))
				.parent(root)
				.build(advancementConsumer, Constants.MODID + ":" + "corn_coblin");

		Advancement encounterChildOfTheCorn = Advancement.Builder.create()
				.display(makeDisplay(SHObjects.CURSED_CORN_1, "child_of_the_corn"))
				.criterion("encounter_child_of_the_corn", new OnEntityTargetPlayerCriteria.Conditions(EntityPredicate.Extended.EMPTY, SHEntityTypes.CHILD_OF_THE_CORN))
				.parent(root)
				.build(advancementConsumer, Constants.MODID + ":" + "child_of_the_corn");
	}
}
