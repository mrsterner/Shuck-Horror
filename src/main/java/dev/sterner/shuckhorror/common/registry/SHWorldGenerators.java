package dev.sterner.shuckhorror.common.registry;

import dev.sterner.shuckhorror.common.entity.ChildOfTheCornEntity;
import dev.sterner.shuckhorror.common.util.Constants;
import dev.sterner.shuckhorror.mixin.accessor.SpawnRestrictionAccessor;
import net.fabricmc.fabric.api.biome.v1.*;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBiomeTags;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placementmodifier.BiomePlacementModifier;
import net.minecraft.world.gen.placementmodifier.RarityFilterPlacementModifier;
import net.minecraft.world.gen.placementmodifier.SquarePlacementModifier;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;

import java.util.List;
import java.util.function.Predicate;

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

	if (registerEntitySpawn(SHEntityTypes.CHILD_OF_THE_CORN,
			BiomeSelectors.foundInOverworld().and(context -> !context.getBiome().getSpawnSettings().getSpawnEntries(SHEntityTypes.CHILD_OF_THE_CORN.getSpawnGroup()).isEmpty()),
			20, 1, 1)) {
			SpawnRestrictionAccessor.callRegister(SHEntityTypes.CHILD_OF_THE_CORN, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, ChildOfTheCornEntity::canSpawn);
		}
	}

	private static boolean registerEntitySpawn(EntityType<?> type, Predicate<BiomeSelectionContext> predicate, int weight, int minGroupSize, int maxGroupSize) {
		if (weight < 0) {
			throw new UnsupportedOperationException("Could not register entity type " + type.getTranslationKey() + ": weight " + weight + " cannot be negative.");
		} else if (minGroupSize < 0) {
			throw new UnsupportedOperationException("Could not register entity type " + type.getTranslationKey() + ": minGroupSize " + minGroupSize + " cannot be negative.");
		} else if (maxGroupSize < 0) {
			throw new UnsupportedOperationException("Could not register entity type " + type.getTranslationKey() + ": maxGroupSize " + maxGroupSize + " cannot be negative.");
		} else if (minGroupSize > maxGroupSize) {
			throw new UnsupportedOperationException("Could not register entity type " + type.getTranslationKey() + ": minGroupSize " + minGroupSize + " cannot be greater than maxGroupSize " + maxGroupSize + ".");
		} else if (weight == 0 || minGroupSize == 0) {
			return false;
		}
		BiomeModifications.addSpawn(predicate, type.getSpawnGroup(), type, weight, minGroupSize, maxGroupSize);
		return true;
	}
}
