package me.sammc19.betteroregen.generation;

import net.minecraft.block.Block;
import net.minecraft.world.biome.Biome;

import java.util.ArrayList;
import java.util.Map;

public class OreVein {

    public Map<Block, Integer> blocks;
    public int yMin;
    public int yMax;
    public int size;
    public float frequency;
    public float density;
    // Not yet sure how the dimensions/biomes are going to be handled exactly.
    // Will likely end up changing this type at some point in the future.
    public ArrayList<Biome> biomes;
    public ArrayList<Integer> dimensions;

    private OreVein(){ }

    public static class Builder {
        private Map<Block, Integer> blocks;
        private int yMin;
        private int yMax;
        private int size;
        private float frequency;
        private float density;
        // Not yet sure how the dimensions/biomes are going to be handled exactly.
        // Will likely end up changing this type at some point in the future.
        private ArrayList<Biome> biomes;
        private ArrayList<Integer> dimensions;

        public Builder() { }

        public Builder withBlocks(Map<Block, Integer> blocks){
            this.blocks = blocks;
            return this;
        }

        public Builder withYMin(int yMin){
            this.yMin = yMin;
            return this;
        }

        public Builder withYMax(int yMax){
            this.yMax = yMax;
            return this;
        }

        public Builder withSize(int size){
            this.size = size;
            return this;
        }

        public Builder withFrequency(float frequency){
            this.frequency = frequency;
            return this;
        }

        public Builder withDensity(float density){
            this.density = density;
            return this;
        }

        public Builder withBiomes(ArrayList<Biome> biomes){
            this.biomes = biomes;
            return this;
        }

        public Builder withDimensions(ArrayList<Integer> dimensions){
            this.dimensions = dimensions;
            return this;
        }

        public OreVein build(){
            //Here we create the actual bank account object, which is always in a fully initialised state when it's returned.
            OreVein oreVein = new OreVein();  //Since the builder is in the BankAccount class, we can invoke its private constructor.
            oreVein.blocks = this.blocks;
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
}
