package dev.sterner.shuckhorror.data.criterion;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
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
import net.minecraft.util.JsonHelper;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class BlockBreakCriteria extends AbstractCriterion<BlockBreakCriteria.Conditions> {
	public static final Identifier ID = Constants.id("block_break");

	@Override
	protected Conditions conditionsFromJson(JsonObject obj, EntityPredicate.Extended playerPredicate, AdvancementEntityPredicateDeserializer predicateDeserializer) {
		Block block = getBlock(obj);
		return new Conditions(playerPredicate, block);
	}

	public void trigger(ServerPlayerEntity player, BlockState block) {
		this.trigger(player, c -> block.isOf(c.getBlock()));
	}

	@Nullable
	private static Block getBlock(JsonObject obj) {
		if (obj.has("block")) {
			Identifier identifier = new Identifier(JsonHelper.getString(obj, "block"));
			return (Block)Registry.BLOCK.getOrEmpty(identifier).orElseThrow(() -> {
				return new JsonSyntaxException("Unknown block type '" + identifier + "'");
			});
		} else {
			return null;
		}
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
			JsonObject jsonObject = super.toJson(predicateSerializer);
			if (this.block != null) {
				jsonObject.addProperty("block", Registry.BLOCK.getId(this.block).toString());
			}
			return jsonObject;
		}

		public Block getBlock() {
			return block;
		}
	}
}
