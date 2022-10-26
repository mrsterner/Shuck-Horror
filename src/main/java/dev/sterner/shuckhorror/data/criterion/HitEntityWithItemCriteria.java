package dev.sterner.shuckhorror.data.criterion;

import com.google.gson.JsonObject;
import dev.sterner.shuckhorror.common.util.Constants;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.DamageSourcePredicate;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class HitEntityWithItemCriteria extends AbstractCriterion<HitEntityWithItemCriteria.Conditions> {
	public static final Identifier ID = Constants.id("attack_taget_with_item");

	@Override
	protected HitEntityWithItemCriteria.Conditions conditionsFromJson(JsonObject obj, EntityPredicate.Extended playerPredicate, AdvancementEntityPredicateDeserializer predicateDeserializer) {
		EntityPredicate.Extended entity = EntityPredicate.Extended.getInJson(obj, "entity", predicateDeserializer);
		ItemPredicate itemPredicate = ItemPredicate.fromJson(obj.get("item"));
		return new HitEntityWithItemCriteria.Conditions(
				playerPredicate,
				itemPredicate,
				DamageSourcePredicate.fromJson(obj.get("killing_blow")),
				entity
		);
	}


	public void trigger(ServerPlayerEntity player, ItemStack stack, @Nullable Entity entity, DamageSource killingDamage) {
		LootContext lootContext = EntityPredicate.createAdvancementEntityLootContext(player, entity);
		this.trigger(player, (conditions) -> conditions.test(player, stack, lootContext, killingDamage));
	}

	@Override
	public Identifier getId() {
		return ID;
	}

	public static class Conditions extends AbstractCriterionConditions {
		private final ItemPredicate item;
		private final DamageSourcePredicate killingBlow;
		private final EntityPredicate.Extended entity;

		public Conditions(EntityPredicate.Extended player, ItemPredicate item, DamageSourcePredicate killingBlow, EntityPredicate.Extended entity) {
			super(HitEntityWithItemCriteria.ID, player);
			this.item = item;
			this.killingBlow = killingBlow;
			this.entity = entity;
		}


		public boolean test(ServerPlayerEntity player, ItemStack stack, LootContext entityContext, DamageSource killingBlow) {
			if (!this.killingBlow.test(player, killingBlow) && !this.item.test(stack)) {
				return false;
			} else {
				return this.entity.test(entityContext);
			}
		}

		public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
			JsonObject jsonObject = super.toJson(predicateSerializer);
			jsonObject.add("item", this.item.toJson());
			jsonObject.add("entity", this.entity.toJson(predicateSerializer));
			jsonObject.add("killing_blow", this.killingBlow.toJson());
			return jsonObject;
		}
	}
}
