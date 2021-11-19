package me.sammc19.betteroregen.generation;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SideShapeType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.util.FeatureContext;

import java.util.Random;
import java.util.Set;

public class VeinGenerator {

    private StructureWorldAccess worldAccess;

    public boolean generate(FeatureContext<OreFeatureConfig> context) {
        Random random = context.getRandom();
        BlockPos blockPos = context.getOrigin();
        this.worldAccess = context.getWorld();
        OreFeatureConfig oreFeatureConfig = context.getConfig();
        int x = blockPos.getX();
        int y = blockPos.getY();
        int z = blockPos.getZ();

        if (y <= worldAccess.getTopY(Heightmap.Type.OCEAN_FLOOR_WG, x, z)) {
            return place(oreFeatureConfig, random, x, y, z);
        }

        return false;
    }

    private boolean place(OreFeatureConfig config, Random random, int x, int y, int z) {
        BlockPos blockPos = new BlockPos(x, y, z);

        if (blockIsFloating(new Block(blockPos)))
            return false;

        worldAccess.setBlockState(blockPos, Blocks.DIAMOND_BLOCK.getDefaultState(), 0);
        return true;
    }

    private Set<VeinGenerator.Block> getAdjacentBlocks(BlockPos blockPos) {
        return Set.of(
                new Block(blockPos.north()),
                new Block(blockPos.south()),
                new Block(blockPos.east()),
                new Block(blockPos.west()),
                new Block(blockPos.up()),
                new Block(blockPos.down())
        );
    }

    private boolean blockIsFloating(Block block) {
        for (Direction direction : Direction.values()) {
            if (block.blockState.isSideSolid(worldAccess, block.blockPos, direction, SideShapeType.FULL)) {
                return false;
            }
        }
        return true;
    }

    private class Block {
        public BlockState blockState;
        public BlockPos blockPos;

        public Block(BlockPos blockPos) {
            this.blockPos = blockPos;
            this.blockState = worldAccess.getBlockState(blockPos);
        }
    }
}