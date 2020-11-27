package me.sammc19.betteroregen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryLookupCodec;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.biome.source.VanillaLayeredBiomeSource;


public class BetterOreBiomeSource extends BiomeSource {

    public static final Codec<BetterOreBiomeSource> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            RegistryLookupCodec.of(Registry.BIOME_KEY).forGetter(source -> source.biomeRegistry),
            Codec.LONG.fieldOf("seed").stable().forGetter(source -> source.seed))
            .apply(instance, instance.stable(BetterOreBiomeSource::new)));

    private final Registry<Biome> biomeRegistry;
    private final long seed;
    private final VanillaLayeredBiomeSource vanillaLayeredBiomeSource;

    public BetterOreBiomeSource(Registry<Biome> biomeRegistry, long seed) {
        super(VanillaLayeredBiomeSource.BIOMES
                .stream()
                .map((biomeRegistryKey -> () -> (Biome) biomeRegistry.get(biomeRegistryKey))));

        this.biomeRegistry = biomeRegistry;
        this.seed = seed;
        this.vanillaLayeredBiomeSource = new VanillaLayeredBiomeSource(seed, false, false, biomeRegistry);
    }

    @Override
    protected Codec<? extends BiomeSource> getCodec() {
        return CODEC;
    }

    @Override
    public BiomeSource withSeed(long seed) {
        return vanillaLayeredBiomeSource.withSeed(seed);
    }

    @Override
    public Biome getBiomeForNoiseGen(int biomeX, int biomeY, int biomeZ) {
        return vanillaLayeredBiomeSource.getBiomeForNoiseGen(biomeX, biomeY, biomeZ);
    }
}
