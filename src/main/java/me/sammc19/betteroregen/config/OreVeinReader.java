package me.sammc19.betteroregen.config;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.sammc19.betteroregen.generation.OreVein;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OreVeinReader {

    private static final int WORLD_HEIGHT = 256;

    public static OreVein readVein(JsonObject jsonObject){
        OreVein oreVein = new OreVein.Builder()
                .withBlocks(readBlocks(jsonObject.get("blocks")))
                .withYMin(readYMin(jsonObject.get("yMin")))
                .withYMax(readYMax(jsonObject.get("yMax")))
                .withSize(readSize(jsonObject.get("size")))
                .withFrequency(readFrequency(jsonObject.get("frequency")))
                .withDensity(readDensity(jsonObject.get("density")))
                .withDimensions(readDimensions(jsonObject.get("dimensions")))
                .withBiomes(readBiomes(jsonObject.get("biomes")))
                .build();

        if(oreVein.yMin > oreVein.yMax){
            throw new IllegalArgumentException("Invalid yMin/yMax. yMax must be greater than yMin.");
        }

        return oreVein;
    }

    private static Map<Block, Integer> readBlocks(JsonElement jsonElement){
        JsonArray jsonArray = jsonElement.getAsJsonArray();

        if(jsonArray.size() % 2 == 1){
            throw new IllegalArgumentException("Invalid blocks array. Blocks array must be even length.");
        }

        Map<Block, Integer> blocks = new HashMap<>();
        int sum = 0;

        for(int i=0; i< jsonArray.size(); i+=2){
            Block block = Registry.BLOCK.get(new Identifier(jsonArray.get(i).getAsString()));
            if(block == Blocks.AIR){
                throw new IllegalArgumentException("Invalid blocks array. Block: \"" + jsonArray.get(i).getAsString() + "\" not found.");
            }
            int blockAmount = jsonArray.get(i+1).getAsInt();
            blocks.put(block, blockAmount);
            sum += blockAmount;
        }

        if(sum != 100){
            throw new IllegalArgumentException("Invalid blocks array. Blocks must sum to 100.");
        }
        return blocks;
    }

    private static ArrayList<Biome> readBiomes(JsonElement jsonElement){
        JsonArray jsonArray = jsonElement.getAsJsonArray();

        ArrayList<Biome> biomes = new ArrayList<>();

        for(int i=0; i< jsonArray.size(); i++){
            //biomes.add(jsonArray.get(i).getAsString());
        }

        return biomes;
    }

    private static ArrayList<Integer> readDimensions(JsonElement jsonElement){
        JsonArray jsonArray = jsonElement.getAsJsonArray();

        ArrayList<Integer> dimensions = new ArrayList<>();

        for(int i=0; i< jsonArray.size(); i++){
            dimensions.add(jsonArray.get(i).getAsInt());
        }

        return dimensions;
    }

    private static int readYMin(JsonElement jsonElement){
        int yMin = jsonElement.getAsInt();
        if(yMin < 0 || yMin > WORLD_HEIGHT){
            throw new IllegalArgumentException("Invalid yMin. yMin must be between 0 and " + WORLD_HEIGHT + ".");
        }
        return yMin;
    }

    private static int readYMax(JsonElement jsonElement){
        int yMax = jsonElement.getAsInt();
        if(yMax < 0 || yMax > WORLD_HEIGHT){
            throw new IllegalArgumentException("Invalid yMax. yMax must be between 0 and " + WORLD_HEIGHT + ".");
        }
        return yMax;
    }

    private static int readSize(JsonElement jsonElement){
        return jsonElement.getAsInt();
    }

    private static float readFrequency(JsonElement jsonElement){
        return jsonElement.getAsInt();
    }

    private static float readDensity(JsonElement jsonElement){
        float density = jsonElement.getAsFloat();
        if(density < 0 || density > 1){
            throw new IllegalArgumentException("Invalid density. density must be between 0 and 1.");
        }
        return density;
    }
}
