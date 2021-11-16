package me.sammc19.betteroregen.generation;

import me.sammc19.betteroregen.util.Registration;
import net.fabricmc.fabric.api.biome.v1.BiomeModificationContext;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.structure.rule.RuleTest;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.decorator.ChanceDecoratorConfig;
import net.minecraft.world.gen.decorator.ConfiguredDecorator;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.OreFeature;
import net.minecraft.world.gen.feature.OreFeatureConfig;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

public class VeinGenerator {

    public OreFeature generateVein(OreVein oreVein, WorldAccess world, BlockPos blockPos, BlockState blockState){

        Decorator<ChanceDecoratorConfig> CHANCE_OCEAN_FLOOR_WG = Registry.register(Registry.DECORATOR, new Identifier("chance_heightmap_legacy"), new TestDecorator());

        ConfiguredDecorator decorator = new ConfiguredDecorator(CHANCE_OCEAN_FLOOR_WG, new ChanceDecoratorConfig(1));

        RuleTest ruleTest = OreFeatureConfig.Rules.BASE_STONE_OVERWORLD;
        BlockState state = Blocks.DIAMOND_BLOCK.getDefaultState();
        int size = 64;

        ConfiguredFeature<?, ?> TEST_FEATURE = new TestFeature()
                .configure(new OreFeatureConfig(ruleTest, state, size))
                .decorate(decorator);

        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, TestFeature.ID, TEST_FEATURE);

        Registration.addToBiome(
                TestFeature.ID,
                BiomeSelectors.categories(Biome.Category.PLAINS,
                        Biome.Category.SWAMP,
                        Biome.Category.SAVANNA,
                        Biome.Category.FOREST,
                        Biome.Category.TAIGA,
                        Biome.Category.ICY,
                        Biome.Category.DESERT,
                        Biome.Category.OCEAN)
                        .and(Registration.booleanToPredicate(true))
                        .and(BiomeSelectors.foundInOverworld()),
                (context) -> Registration.addFeature(context, TEST_FEATURE)
        );

        return null;
    }
}