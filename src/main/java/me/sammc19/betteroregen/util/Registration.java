package me.sammc19.betteroregen.util;

import net.fabricmc.fabric.api.biome.v1.BiomeModificationContext;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.ConfiguredFeature;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class Registration {

    public static void addFeature(BiomeModificationContext context, ConfiguredFeature<?, ?> feature) {
        context.getGenerationSettings().addBuiltInFeature(GenerationStep.Feature.UNDERGROUND_ORES, feature);
    }

    public static void addToBiome(Identifier id, Predicate<BiomeSelectionContext> selectorPredicate, Consumer<BiomeModificationContext> biomeAdditionConsumer) {
        BiomeModifications.create(id).add(ModificationPhase.ADDITIONS, selectorPredicate, biomeAdditionConsumer);
    }

    public static Predicate<BiomeSelectionContext> booleanToPredicate(boolean bol) {
        return (context) -> bol;
    }
}
