package dev.sterner.shuckhorror.data.criterion;

import com.google.gson.JsonObject;
import dev.sterner.shuckhorror.common.util.Constants;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Objects;

public class BlockBreakCriteria extends AbstractCriterion<BlockBreakCriteria.Conditions> {
	public static final Identifier ID = Constants.id("block_break");

	@Override
	protected Conditions conditionsFromJson(JsonObject obj, EntityPredicate.Extended playerPredicate, AdvancementEntityPredicateDeserializer predicateDeserializer) {
		return new Conditions(playerPredicate, Registry.BLOCK.get(Identifier.tryParse(obj.get("block").getAsString())));
	}

	public void trigger(ServerPlayerEntity player, BlockState block) {
		this.trigger(player, c -> block.isOf(c.getBlock()));
	}

	@Override
	public Identifier getId() {
		return ID;
	}

	public static class Conditions extends AbstractCriterionConditions {
		private final Block block;

		public Conditions(EntityPredicate.Extended playerPredicate, Block blockTag) {
			super(ID, playerPredicate);
			this.block = Objects.requireNonNull(blockTag);
		}

		@Override
		public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
			JsonObject json = new JsonObject();
			json.addProperty("block", block.toString());
			return super.toJson(predicateSerializer);
		}

		public Block getBlock() {
			return block;
		}
	}
}
