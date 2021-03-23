package me.sammc19.betteroregen.generation;

import me.sammc19.betteroregen.BetterOreGen;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;

import java.util.ArrayList;

public class OreRemover {

    private static final BlockState STONE = Blocks.STONE.getDefaultState();

    private WorldAccess world;

    public OreRemover(){}

    public BlockState removeOre(WorldAccess world, BlockPos blockPos, BlockState blockState){
        this.world = world;
        if(BetterOreGen.ores.contains(blockState)){
            ArrayList<BlockState> adjacentBlocks = getAdjacentBlocks(blockPos);
            for(BlockState block : BetterOreGen.stones){
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
