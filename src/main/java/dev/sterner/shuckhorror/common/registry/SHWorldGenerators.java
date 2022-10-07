package dev.sterner.shuckhorror.common.registry;

import dev.sterner.shuckhorror.common.util.Constants;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBiomeTags;
import net.minecraft.util.Holder;
import net.minecraft.util.math.intprovider.ClampedIntProvider;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.decorator.BiomePlacementModifier;
import net.minecraft.world.gen.decorator.CountPlacementModifier;
import net.minecraft.world.gen.decorator.InSquarePlacementModifier;
import net.minecraft.world.gen.decorator.RarityFilterPlacementModifier;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.util.ConfiguredFeatureUtil;
import net.minecraft.world.gen.feature.util.PlacedFeatureUtil;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import org.quiltmc.qsl.worldgen.biome.api.BiomeModification;
import org.quiltmc.qsl.worldgen.biome.api.BiomeModifications;
import org.quiltmc.qsl.worldgen.biome.api.BiomeSelectors;
import org.quiltmc.qsl.worldgen.biome.api.ModificationPhase;

public class SHWorldGenerators {

	public static final Holder<ConfiguredFeature<RandomPatchFeatureConfig, ?>> CONFIGURED_PATCH_WILD_MAIZE =
			ConfiguredFeatureUtil.register(
					"wild_maize",
					Feature.FLOWER,
			new RandomPatchFeatureConfig(
					64,
					6,
					2,
					PlacedFeatureUtil.onlyWhenEmpty(Feature.SIMPLE_BLOCK, new SimpleBlockFeatureConfig(BlockStateProvider.of(SHObjects.WILD_MAIZE))))
			);

	public static final Holder<PlacedFeature> PATCH_WILD_MAIZE = PlacedFeatureUtil.register("patch_wild_maize",
			CONFIGURED_PATCH_WILD_MAIZE,
			RarityFilterPlacementModifier.create(2),
			InSquarePlacementModifier.getInstance(),
			CountPlacementModifier.create(ClampedIntProvider.create(UniformIntProvider.create(-3, 1), 0, 1)),
			PlacedFeatureUtil.MOTION_BLOCKING_HEIGHTMAP, BiomePlacementModifier.getInstance());


	public static void init(){
		BiomeModification worldGen = BiomeModifications.create(Constants.id("world_features"));
		worldGen.add(
				ModificationPhase.ADDITIONS,
				BiomeSelectors.isIn(ConventionalBiomeTags.JUNGLE).or(BiomeSelectors.isIn(ConventionalBiomeTags.PLAINS)),
				context -> context.getGenerationSettings().addBuiltInFeature(GenerationStep.Feature.VEGETAL_DECORATION, PATCH_WILD_MAIZE.value()));

	}
}
