package me.sammc19.betteroregen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.BlockView;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.Heightmap;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.chunk.NoiseChunkGenerator;
import net.minecraft.world.gen.chunk.StructuresConfig;

public class BetterOreChunkGenerator extends ChunkGenerator {

    public static final Codec<BetterOreChunkGenerator> CODEC = RecordCodecBuilder.create(
            (instance) -> instance.group(
            BiomeSource.CODEC
                    .fieldOf("biome_source")
                    .forGetter((generator) -> generator.biomeSource),
            Codec.LONG.fieldOf("seed")
                    .stable()
                    .forGetter((generator) -> generator.seed))
            .apply(instance, instance.stable(BetterOreChunkGenerator::new)));

    private final long seed;
    private final BiomeSource biomeSource;

    private final NoiseChunkGenerator noiseChunkGenerator;

    public BetterOreChunkGenerator(BiomeSource biomeSource, long seed) {
        super(biomeSource, new StructuresConfig(true));

        this.biomeSource = biomeSource;
        this.seed = seed;
        this.noiseChunkGenerator = new NoiseChunkGenerator(this.biomeSource, this.seed, ChunkGeneratorSettings::getInstance);
    }

    @Override
    protected Codec<? extends ChunkGenerator> getCodec() {
        return CODEC;
    }

    @Override
    public ChunkGenerator withSeed(long seed) {
        return noiseChunkGenerator.withSeed(seed);
    }

    @Override
    public void buildSurface(ChunkRegion region, Chunk chunk) {
        noiseChunkGenerator.buildSurface(region, chunk);
    }

    @Override
    public void populateNoise(WorldAccess world, StructureAccessor accessor, Chunk chunk) {
        noiseChunkGenerator.populateNoise(world, accessor, chunk);
    }

    @Override
    public int getHeight(int x, int z, Heightmap.Type heightmapType) {
        return noiseChunkGenerator.getHeight(x, z, heightmapType);
    }

    @Override
    public BlockView getColumnSample(int x, int z) {
        return noiseChunkGenerator.getColumnSample(x, z);
    }

    public void populateEntities(ChunkRegion region) {
        noiseChunkGenerator.populateEntities(region);
    }
}
