package dev.sterner.shuckhorror.common.registry;

import dev.sterner.shuckhorror.common.util.Constants;
import net.fabricmc.fabric.api.biome.v1.BiomeModification;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBiomeTags;
import net.minecraft.block.Blocks;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.collection.DataPool;
import net.minecraft.util.math.VerticalSurfaceType;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placementmodifier.BiomePlacementModifier;
import net.minecraft.world.gen.placementmodifier.RarityFilterPlacementModifier;
import net.minecraft.world.gen.placementmodifier.SquarePlacementModifier;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.stateprovider.WeightedBlockStateProvider;

import java.util.List;

import static net.minecraft.world.gen.feature.UndergroundConfiguredFeatures.MOSS_VEGETATION;

public class SHWorldGenerators {

	//Configured Features
	public static final RegistryEntry<ConfiguredFeature<RandomPatchFeatureConfig, ?>> CONFIGURED_PATCH_WILD_MAIZE = ConfiguredFeatures.register(
			"wild_maize",
			Feature.RANDOM_PATCH,
			ConfiguredFeatures.createRandomPatchFeatureConfig(
					Feature.SIMPLE_BLOCK, new SimpleBlockFeatureConfig(BlockStateProvider.of(SHObjects.WILD_MAIZE)), List.of(Blocks.GRASS_BLOCK)
			)
	);

	//Placed Features
	public static final RegistryEntry<PlacedFeature> PATCH_WILD_MAIZE = PlacedFeatures.register(
			"patch_wild_maize",
			CONFIGURED_PATCH_WILD_MAIZE,
			RarityFilterPlacementModifier.of(30),
			SquarePlacementModifier.of(),
			PlacedFeatures.MOTION_BLOCKING_HEIGHTMAP,
			BiomePlacementModifier.of()
	);

	public static void init(){
		BiomeModification worldGen = BiomeModifications.create(Constants.id("world_features"));
		worldGen.add(
				ModificationPhase.ADDITIONS,
				BiomeSelectors.tag(ConventionalBiomeTags.JUNGLE).or(BiomeSelectors.tag(ConventionalBiomeTags.PLAINS)),
				context -> context.getGenerationSettings().addBuiltInFeature(GenerationStep.Feature.VEGETAL_DECORATION, PATCH_WILD_MAIZE.value()));

	}
}
