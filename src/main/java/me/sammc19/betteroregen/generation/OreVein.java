package me.sammc19.betteroregen.generation;

import net.minecraft.block.Block;
import net.minecraft.world.biome.Biome;

import java.util.ArrayList;

public class OreVein {

    public String name;
    public ArrayList<Block> blocks;
    public ArrayList<Integer> blockWeights;
    public ArrayList<Block> replaceableBlocks;
    public int yMin;
    public int yMax;
    public int size;
    public double frequency;
    public double density;
    // Not yet sure how the dimensions/biomes are going to be handled exactly.
    // Will likely end up changing this type at some point in the future.
    public ArrayList<Biome> biomes;
    public ArrayList<Integer> dimensions;

    private OreVein() {
    }

    public static class Builder {

        public String name;
        public ArrayList<Block> blocks;
        public ArrayList<Integer> blockWeights;
        public ArrayList<Block> replaceableBlocks;
        private int yMin;
        private int yMax;
        private int size;
        private double frequency;
        private double density;
        // Not yet sure how the dimensions/biomes are going to be handled exactly.
        // Will likely end up changing this type at some point in the future.
        private ArrayList<Biome> biomes;
        private ArrayList<Integer> dimensions;

        public Builder() {
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withBlocks(ArrayList<Block> blocks) {
            this.blocks = blocks;
            return this;
        }

        public Builder withBlockWeights(ArrayList<Integer> blockWeights) {
            this.blockWeights = blockWeights;
            return this;
        }

        public Builder withReplaceableBlocks(ArrayList<Block> replaceableBlocks) {
            this.replaceableBlocks = replaceableBlocks;
            return this;
        }

        public Builder withYMin(int yMin) {
            this.yMin = yMin;
            return this;
        }

        public Builder withYMax(int yMax) {
            this.yMax = yMax;
            return this;
        }

        public Builder withSize(int size) {
            this.size = size;
            return this;
        }

        public Builder withFrequency(double frequency) {
            this.frequency = frequency;
            return this;
        }

        public Builder withDensity(double density) {
            this.density = density;
            return this;
        }

        public Builder withBiomes(ArrayList<Biome> biomes) {
            this.biomes = biomes;
            return this;
        }

        public Builder withDimensions(ArrayList<Integer> dimensions) {
            this.dimensions = dimensions;
            return this;
        }

        public OreVein build() {
            OreVein oreVein = new OreVein();
            oreVein.name = this.name;
            oreVein.blocks = this.blocks;
            oreVein.blockWeights = this.blockWeights;
            oreVein.replaceableBlocks = this.replaceableBlocks;
            oreVein.yMin = this.yMin;
            oreVein.yMax = this.yMax;
            oreVein.size = this.size;
            oreVein.frequency = this.frequency;
            oreVein.density = this.density;
            oreVein.biomes = this.biomes;
            oreVein.dimensions = this.dimensions;

            return oreVein;
        }
    }

    public String toString() {
        return String.format("OreVein{%s}=(blocks=%s, blockWeights=%s, replaceableBlocks=%s yMin=%d, yMax=%d, size=%d, frequency=%f, density=%f, biomes=%s, dimensions=%s)",
                this.name,
                this.blocks.toString(),
                this.blockWeights.toString(),
                this.replaceableBlocks.toString(),
                this.yMin,
                this.yMax,
                this.size,
                this.frequency,
                this.density,
                this.biomes.toString(),
                this.dimensions.toString());
    }
}
