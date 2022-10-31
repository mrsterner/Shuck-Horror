package dev.sterner.shuckhorror.data;

import dev.sterner.shuckhorror.common.registry.SHObjects;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.block.Blocks;
import net.minecraft.data.server.recipe.CookingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.tag.ItemTags;

import java.util.function.Consumer;

public class SHRecipeProvider extends FabricRecipeProvider {
	public SHRecipeProvider(FabricDataGenerator dataGenerator) {
		super(dataGenerator);
	}

	@Override
	protected void generateRecipes(Consumer<RecipeJsonProvider> exporter) {
		/*TODO
		ShapedRecipeJsonBuilder.create(SHObjects.CORN_BREAD)
				.input('h', Items.HONEY_BOTTLE)
				.input('c', SHObjects.CORN_COB_1)
				.input('e', Items.EGG)
				.input('m', Items.MILK_BUCKET)
				.pattern("hhh")
				.pattern("cec")
				.pattern("cmc")
				.criterion("has_corn", conditionsFromItem(SHObjects.CORN_COB_1))
				.offerTo(exporter);

		 */

		ShapedRecipeJsonBuilder.create(SHObjects.GARMONBOZIA)
				.input('c', SHObjects.CURSED_CORN_1)
				.input('o', SHObjects.CORN_COB_1)
				.input('p', Items.PHANTOM_MEMBRANE)
				.input('b', Items.BOWL)
				.pattern("ccc")
				.pattern("coc")
				.pattern("pbp")
				.criterion("has_corn", conditionsFromItem(SHObjects.CORN_COB_1))
				.offerTo(exporter);

		ShapedRecipeJsonBuilder.create(SHObjects.SCYTHE)
				.input('n', Items.NETHERITE_INGOT)
				.input('s', SHObjects.SICKLE)
				.input('d', Items.DARK_OAK_SAPLING)
				.pattern("ns")
				.pattern("d ")
				.pattern("d ")
				.criterion("has_sickle", conditionsFromItem(SHObjects.SICKLE))
				.offerTo(exporter);


		ShapedRecipeJsonBuilder.create(Items.TORCH, 4)
				.input('c', SHObjects.CORN_COB_1)
				.input('s', Items.STICK)
				.pattern("c")
				.pattern("s")
				.criterion("has_corn", conditionsFromItem(SHObjects.CORN_COB_1))
				.offerTo(exporter);

		ShapedRecipeJsonBuilder.create(Blocks.CAMPFIRE)
				.input('L', ItemTags.LOGS)
				.input('S', (ItemConvertible)Items.STICK)
				.input('C', SHObjects.CORN_COB_1)
				.pattern(" S ")
				.pattern("SCS")
				.pattern("LLL")
				.criterion("has_stick", conditionsFromItem(Items.STICK))
				.offerTo(exporter);

		ShapedRecipeJsonBuilder.create(Blocks.SOUL_CAMPFIRE)
				.input('L', ItemTags.LOGS)
				.input('S', (ItemConvertible)Items.STICK)
				.input('C', SHObjects.CURSED_CORN_1)
				.pattern(" S ")
				.pattern("SCS")
				.pattern("LLL")
				.criterion("has_stick", conditionsFromItem(Items.STICK))
				.offerTo(exporter);

		ShapedRecipeJsonBuilder.create(Items.SOUL_TORCH, 4)
				.input('c', SHObjects.CURSED_CORN_1)
				.input('s', Items.STICK)
				.pattern("c")
				.pattern("s")
				.criterion("has_corn", conditionsFromItem(SHObjects.CORN_COB_1))
				.offerTo(exporter);

		ShapelessRecipeJsonBuilder.create(SHObjects.CORN_KERNELS, 3)
				.input(SHObjects.CORN_COB_1)
				.criterion("has_corn", conditionsFromItem(SHObjects.CORN_COB_1))
				.offerTo(exporter);
		ShapelessRecipeJsonBuilder.create(SHObjects.CURSED_CORN_KERNELS, 3)
				.input(SHObjects.CURSED_CORN_1)
				.criterion("has_corn", conditionsFromItem(SHObjects.CORN_COB_1))
				.offerTo(exporter);

		ShapelessRecipeJsonBuilder.create(SHObjects.CORN_COB_1, 1)
				.input(SHObjects.CORN_KERNELS, 3)
				.criterion("has_corn_kernel", conditionsFromItem(SHObjects.CORN_KERNELS))
				.offerTo(exporter);
		ShapelessRecipeJsonBuilder.create(SHObjects.CURSED_CORN_1, 1)
				.input(SHObjects.CURSED_CORN_KERNELS, 3)
				.criterion("has_corn_kernel", conditionsFromItem(SHObjects.CORN_KERNELS))
				.offerTo(exporter);




		CookingRecipeJsonBuilder.createSmelting(
				Ingredient.ofItems(SHObjects.CORN_KERNELS), SHObjects.POPCORN, 0.1F, 200)
				.criterion("has_corn_kernel", conditionsFromItem(SHObjects.CORN_KERNELS)).offerTo(exporter);;

		CookingRecipeJsonBuilder.createSmelting(
				Ingredient.ofItems(SHObjects.CORN_COB_1), SHObjects.ROASTED_CORN_1, 0.1F, 200)
				.criterion("has_corn", conditionsFromItem(SHObjects.CORN_COB_1)).offerTo(exporter);;

		offerCookingRecipe(exporter, "smoking", RecipeSerializer.SMOKING, 100, SHObjects.CORN_COB_1, SHObjects.ROASTED_CORN_1, 0.35F);
		offerCookingRecipe(exporter, "campfire_cooking", RecipeSerializer.CAMPFIRE_COOKING, 600, SHObjects.CORN_COB_1, SHObjects.ROASTED_CORN_1, 0.35F);
		offerCookingRecipe(exporter, "smoking", RecipeSerializer.SMOKING, 100, SHObjects.CORN_KERNELS, SHObjects.POPCORN, 0.35F);
		offerCookingRecipe(exporter, "campfire_cooking", RecipeSerializer.CAMPFIRE_COOKING, 600, SHObjects.CORN_KERNELS, SHObjects.POPCORN, 0.35F);



	}
}
