package me.sammc19.betteroregen;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;

import java.util.ArrayList;

public class OreRemover {

    //TODO: move list of ores into some config/JSON and add a method/class to read those in on init
    public static final ArrayList<BlockState> ORES = new ArrayList<>();
    private static final ArrayList<BlockState> BASIC_BLOCKS = new ArrayList<>();

    private static final BlockState STONE = Blocks.STONE.getDefaultState();

    private WorldAccess world;

    public OreRemover(){

        ORES.add(Blocks.COAL_ORE.getDefaultState());
        ORES.add(Blocks.DIAMOND_ORE.getDefaultState());
        ORES.add(Blocks.EMERALD_ORE.getDefaultState());
        ORES.add(Blocks.GOLD_ORE.getDefaultState());
        ORES.add(Blocks.IRON_ORE.getDefaultState());
        ORES.add(Blocks.LAPIS_ORE.getDefaultState());
        ORES.add(Blocks.REDSTONE_ORE.getDefaultState());

        BASIC_BLOCKS.add(Blocks.BEDROCK.getDefaultState());
        BASIC_BLOCKS.add(Blocks.STONE.getDefaultState());
        BASIC_BLOCKS.add(Blocks.GRANITE.getDefaultState());
        BASIC_BLOCKS.add(Blocks.DIORITE.getDefaultState());
        BASIC_BLOCKS.add(Blocks.DIRT.getDefaultState());
    }

    public BlockState removeOre(WorldAccess world, BlockPos blockPos, BlockState blockState){
        this.world = world;
        if(ORES.contains(blockState)){
            ArrayList<BlockState> adjacentBlocks = getAdjacentBlocks(blockPos);
            for(BlockState block : BASIC_BLOCKS){
                if(adjacentBlocks.contains(block)){
                    return block;
                }
            }
            return STONE;
        }
        return blockState;
    }

    private ArrayList<BlockState> getAdjacentBlocks(BlockPos blockPos){
        ArrayList<BlockState> adjacentBlocks = new ArrayList<>();

        addBlockToList(adjacentBlocks, blockPos.north());
        addBlockToList(adjacentBlocks, blockPos.south());
        addBlockToList(adjacentBlocks, blockPos.east());
        addBlockToList(adjacentBlocks, blockPos.west());
        addBlockToList(adjacentBlocks, blockPos.up());
        addBlockToList(adjacentBlocks, blockPos.down());

        return adjacentBlocks;
    }

    private void addBlockToList(ArrayList<BlockState> blockStates, BlockPos newBlock){
        try{
            blockStates.add(world.getBlockState(newBlock));
        } catch(NullPointerException e){ }
    }

}
