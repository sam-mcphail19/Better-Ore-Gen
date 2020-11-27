package me.sammc19.betteroregen;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class BetterOreGen implements ModInitializer {

    public static final String namespace = "betteroregen";
    public static BetterOreGeneratorType betterOreGeneratorType;

    @Override
    public void onInitialize() {
        betterOreGeneratorType = new BetterOreGeneratorType();

        Registry.register(Registry.CHUNK_GENERATOR, new Identifier(namespace, "chunkgenerator"), BetterOreChunkGenerator.CODEC);
        Registry.register(Registry.BIOME_SOURCE, new Identifier(namespace, "biomesource"), BetterOreBiomeSource.CODEC);

    }
}
