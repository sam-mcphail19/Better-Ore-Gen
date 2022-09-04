package me.sammc19.betteroregen.mixin;

import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.ConfiguredFeatures;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import org.spongepowered.asm.mixin.Mixin;

import static me.sammc19.betteroregen.config.BetterOreGenConfig.oreFeaturesToRemove;


@Mixin(ConfiguredFeatures.class)
public abstract class ConfiguredFeaturesMixin {

    private static <FC extends FeatureConfig> ConfiguredFeature<FC, ?> register(String id, ConfiguredFeature<FC, ?> configuredFeature) {
        if (oreFeaturesToRemove.contains(id)) {
            configuredFeature = (ConfiguredFeature<FC, ?>) Feature.NO_OP.configure(new DefaultFeatureConfig());
        }
        return Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, id, configuredFeature);
    }
}
