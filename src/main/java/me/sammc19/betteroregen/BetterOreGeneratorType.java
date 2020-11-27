package me.sammc19.betteroregen;

import net.minecraft.client.world.GeneratorType;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;

public class BetterOreGeneratorType extends GeneratorType {

    public BetterOreGeneratorType() {
        super("betteroregen");
        GeneratorType.VALUES.add(this);
    }

    @Override
    protected ChunkGenerator getChunkGenerator(Registry<Biome> biomeRegistry, Registry<ChunkGeneratorSettings> chunkGeneratorSettingsRegistry, long seed) {
        return new BetterOreChunkGenerator(new BetterOreBiomeSource(biomeRegistry, seed), seed);
    }

}
