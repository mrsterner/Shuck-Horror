package dev.sterner.shuckhorror.data.criterion;

import com.google.gson.JsonObject;
import dev.sterner.shuckhorror.common.util.Constants;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.predicate.DamagePredicate;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.DamageSourcePredicate;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class HitEntityWithItemCriteria extends AbstractCriterion<HitEntityWithItemCriteria.Conditions> {
	public static final Identifier ID = Constants.id("attack_taget_with_item");

	@Override
	protected HitEntityWithItemCriteria.Conditions conditionsFromJson(JsonObject obj, EntityPredicate.Extended playerPredicate, AdvancementEntityPredicateDeserializer predicateDeserializer) {
		EntityPredicate.Extended entity = EntityPredicate.Extended.getInJson(obj, "entity", predicateDeserializer);
		return new HitEntityWithItemCriteria.Conditions(
				playerPredicate,
				Registry.ITEM.get(Identifier.tryParse(obj.get("item").getAsString())).getDefaultStack(),
				DamageSourcePredicate.fromJson(obj.get("killing_blow")),
				entity
		);
	}

	public void trigger(ServerPlayerEntity player, Item item, Entity entity, DamageSource killingDamage) {
		LootContext lootContext = EntityPredicate.createAdvancementEntityLootContext(player, entity);
		this.trigger(player, (conditions) -> conditions.test(player, item, lootContext, killingDamage));
	}

	@Override
	public Identifier getId() {
		return ID;
	}

	public static class Conditions extends AbstractCriterionConditions {
		private final ItemStack stack;
		private final DamageSourcePredicate killingBlow;
		private final EntityPredicate.Extended entity;

		public Conditions(EntityPredicate.Extended player, ItemStack stack, DamageSourcePredicate killingBlow, EntityPredicate.Extended entity) {
			super(HitEntityWithItemCriteria.ID, player);
			this.stack = stack;
			this.killingBlow = killingBlow;
			this.entity = entity;
		}

		public boolean test(ServerPlayerEntity player, Item item,  LootContext killedEntityContext, DamageSource killingBlow) {
			return this.killingBlow.test(player, killingBlow) && this.entity.test(killedEntityContext);
		}

		public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
			JsonObject jsonObject = super.toJson(predicateSerializer);
			jsonObject.addProperty("stack", stack.toString());
			jsonObject.add("killing_blow", this.killingBlow.toJson());
			jsonObject.add("entity", this.entity.toJson(predicateSerializer));
			return jsonObject;
		}
	}
}
