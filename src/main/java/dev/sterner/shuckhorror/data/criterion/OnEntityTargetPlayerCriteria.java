package dev.sterner.shuckhorror.data.criterion;

import com.google.gson.JsonObject;
import dev.sterner.shuckhorror.common.util.Constants;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Objects;

public class OnEntityTargetPlayerCriteria extends AbstractCriterion<OnEntityTargetPlayerCriteria.Conditions> {
	public static final Identifier ID = Constants.id("entity_target");

	@Override
	protected OnEntityTargetPlayerCriteria.Conditions conditionsFromJson(JsonObject obj, EntityPredicate.Extended playerPredicate, AdvancementEntityPredicateDeserializer predicateDeserializer) {
		return new OnEntityTargetPlayerCriteria.Conditions(playerPredicate, Registry.ENTITY_TYPE.get(Identifier.tryParse(obj.get("livingEntity").getAsString())));
	}

	public void trigger(ServerPlayerEntity player, EntityType entityType) {
		this.trigger(player, c -> entityType == c.getEntity());
	}

	@Override
	public Identifier getId() {
		return ID;
	}

	public static class Conditions extends AbstractCriterionConditions {
		private final EntityType livingEntity;

		public Conditions(EntityPredicate.Extended playerPredicate, EntityType livingEntity) {
			super(ID, playerPredicate);
			this.livingEntity = Objects.requireNonNull(livingEntity);
		}

		@Override
		public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
			JsonObject json = new JsonObject();
			json.addProperty("livingEntity", livingEntity.toString());
			return super.toJson(predicateSerializer);
		}

		public EntityType getEntity() {
			return livingEntity;
		}
	}
}
