package dev.sterner.shuckhorror.data.criterion;

import com.google.gson.JsonObject;
import dev.sterner.shuckhorror.common.util.Constants;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class OnEntityTargetPlayerCriteria extends AbstractCriterion<OnEntityTargetPlayerCriteria.Conditions> {
	public static final Identifier ID = Constants.id("entity_target");

	@Override
	protected OnEntityTargetPlayerCriteria.Conditions conditionsFromJson(JsonObject obj, EntityPredicate.Extended playerPredicate, AdvancementEntityPredicateDeserializer predicateDeserializer) {
		EntityPredicate.Extended entity = EntityPredicate.Extended.getInJson(obj, "entity", predicateDeserializer);
		return new OnEntityTargetPlayerCriteria.Conditions(playerPredicate, entity);
	}

	public void trigger(ServerPlayerEntity player, @Nullable Entity entity) {
		LootContext lootContext = EntityPredicate.createAdvancementEntityLootContext(player, entity);
		this.trigger(player, (conditions) -> conditions.test(player, lootContext));
	}

	@Override
	public Identifier getId() {
		return ID;
	}

	public static class Conditions extends AbstractCriterionConditions {
		private final EntityPredicate.Extended entity;

		public Conditions(EntityPredicate.Extended playerPredicate, EntityPredicate.Extended entity) {
			super(ID, playerPredicate);
			this.entity = Objects.requireNonNull(entity);
		}

		public static OnEntityTargetPlayerCriteria.Conditions create(EntityPredicate.Extended player, EntityPredicate.Extended entity) {
			return new OnEntityTargetPlayerCriteria.Conditions(player, entity);
		}

		public boolean test(ServerPlayerEntity player, LootContext entityContext) {
			return this.entity.test(entityContext);
		}

		@Override
		public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
			JsonObject json = new JsonObject();
			json.addProperty("entity", entity.toString());
			return super.toJson(predicateSerializer);
		}

		public EntityPredicate.Extended getEntity() {
			return entity;
		}
	}
}
