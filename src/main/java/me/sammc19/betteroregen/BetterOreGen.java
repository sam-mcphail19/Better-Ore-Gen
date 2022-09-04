package me.sammc19.betteroregen;

import java.util.ArrayList;
import me.sammc19.betteroregen.config.BetterOreGenConfig;
import me.sammc19.betteroregen.generation.OreVein;
import me.sammc19.betteroregen.generation.VeinGenerator;
import me.sammc19.betteroregen.generation.feature.BetterOreGenOreFeatureConfig;
import me.sammc19.betteroregen.util.StringUtils;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class BetterOreGen implements ModInitializer {

    public static final String namespace = "betteroregen";
    public static final Logger LOGGER = LogManager.getLogger();
    public static final VeinGenerator veinGenerator = new VeinGenerator();
    public static ArrayList<OreVein> oreVeins = new ArrayList<>();

    private static void registerOreVeins() {
        oreVeins.forEach(oreVein -> {
            ConfiguredFeature<?, ?> configuredFeature = Feature.ORE
                .configure(new BetterOreGenOreFeatureConfig(new ArrayList<>(), 0))
                .uniformRange(YOffset.fixed(oreVein.yMin), YOffset.fixed(oreVein.yMax))
                .spreadHorizontally()
                .repeat(50);
            RegistryKey<ConfiguredFeature<?, ?>> oreVeinKey = RegistryKey.of(Registry.CONFIGURED_FEATURE_KEY, new Identifier(namespace, StringUtils.toSnakeCase(oreVein.name)));
            Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, oreVeinKey.getValue(), configuredFeature);
            BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES, oreVeinKey);
        });
    }

    @Override
    public void onInitialize() {
        BetterOreGenConfig.init();
        registerOreVeins();
    }
}
