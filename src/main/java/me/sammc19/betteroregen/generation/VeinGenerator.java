package me.sammc19.betteroregen.generation;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import me.sammc19.betteroregen.BetterOreGen;
import me.sammc19.betteroregen.config.BetterOreGenConfig;
import me.sammc19.betteroregen.util.math.Vector4;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SideShapeType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.ChunkSectionCache;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.WorldChunk;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.util.FeatureContext;


// Command for in game testing: /fill ~-8 0 ~-8 ~8 ~ ~8 minecraft:air replace minecraft:stone
public class VeinGenerator {

    public static Map<String, OreVeinInstance> veinMap;
    public StructureWorldAccess worldAccess;

    public static OreVeinInstance getFromVeinMap(BlockPos blockPos) {
        if (veinMap == null) {
            veinMap = new HashMap<>();
        }

        return veinMap.get(blockPos.toShortString());
    }

    public boolean generateVein(FeatureContext<OreFeatureConfig> context) {
        Random random = context.getRandom();
        BlockPos blockPos = context.getOrigin();
        worldAccess = context.getWorld();

        OreVein oreVein = selectOreVein(blockPos.getY());
        if (oreVein == null) {
            return false;
        }

        OreVeinInstance oreVeinInstance = new OreVeinInstance(oreVein);

        double f = random.nextDouble() * Math.PI;
        double g = oreVein.size / 8.0F;
        int i = MathHelper.ceil((oreVein.size / 8f + 1) / 2f);
        double startX = blockPos.getX() + Math.sin(f) * g;
        double endX = blockPos.getX() - Math.sin(f) * g;
        double startZ = blockPos.getZ() + Math.cos(f) * g;
        double endZ = blockPos.getZ() - Math.cos(f) * g;
        double startY = blockPos.getY() + random.nextInt(3) - 2;
        double endY = blockPos.getY() + random.nextInt(3) - 2;
        int x = blockPos.getX() - MathHelper.ceil(g) - i;
        int y = blockPos.getY() - 2 - i;
        int z = blockPos.getZ() - MathHelper.ceil(g) - i;
        int horizontalSize = 2 * (MathHelper.ceil(g) + i);
        int verticalSize = 2 * (2 + i);

        BlockPos startPos = new BlockPos(startX, startY, startZ);
        BlockPos endPos = new BlockPos(endX, endY, endZ);

        for (int currentX = x; currentX <= x + horizontalSize; currentX++) {
            for (int currentZ = z; currentZ <= z + horizontalSize; currentZ++) {
                if (y <= worldAccess.getTopY(Heightmap.Type.OCEAN_FLOOR_WG, currentX, currentZ)) {
                    return generateVeinPart(startPos, endPos, new BlockPos(x, y, z), horizontalSize, verticalSize, oreVeinInstance);
                }
            }
        }

        return false;
    }

    public boolean generateVeinPart(BlockPos startPos, BlockPos endPos, BlockPos origin, int horizontalSize, int verticalSize, OreVeinInstance oreVeinInstance) {
        int blocksPlaced = 0;
        OreVein vein = oreVeinInstance.veinType;
        BitSet bitSet = new BitSet(horizontalSize * verticalSize * horizontalSize);
        ArrayList<Vector4> vectors = new ArrayList<>();

        double currentX;
        double currentY;
        double currentZ;
        double h;
        for (int i = 0; i < vein.size; i++) {
            float delta = (float) i / vein.size;
            currentX = MathHelper.lerp(delta, startPos.getX(), endPos.getX());
            currentY = MathHelper.lerp(delta, startPos.getY(), endPos.getY());
            currentZ = MathHelper.lerp(delta, startPos.getZ(), endPos.getZ());
            h = Math.random() * vein.size / 16d;
            double l = ((MathHelper.sin((float) Math.PI * delta) + 1) * h + 1) / 2d;
            vectors.add(new Vector4(currentX, currentY, currentZ, l));
        }

        for (int i = 0; i < vein.size - 1; i++) {
            if (vectors.get(i).w > 0) {
                for (int j = i + 1; j < vein.size; j++) {
                    if (vectors.get(j).w > 0) {
                        Vector4 newVector = vectors.get(i).subtract(vectors.get(j));
                        if (newVector.w * newVector.w > newVector.x * newVector.x + newVector.y * newVector.y + newVector.z * newVector.z) {
                            if (newVector.w > 0) {
                                vectors.get(j).w = -1;
                            } else {
                                vectors.get(i).w = -1;
                            }
                        }
                    }
                }
            }
        }

        ChunkSectionCache chunkSectionCache = new ChunkSectionCache(worldAccess);

        try {
            for (int i = 0; i < vein.size; i++) {
                if (vectors.get(i).w < 0) {
                    continue;
                }
                currentX = vectors.get(i).w;
                currentY = vectors.get(i).x;
                currentZ = vectors.get(i).y;
                h = vectors.get(i).z;
                int l = Math.max(MathHelper.floor(currentY - currentX), origin.getX());
                int m = Math.max(MathHelper.floor(currentZ - currentX), origin.getY());
                int n = Math.max(MathHelper.floor(h - currentX), origin.getZ());
                int o = Math.max(MathHelper.floor(currentY + currentX), l);
                int p = Math.max(MathHelper.floor(currentZ + currentX), m);
                int q = Math.max(MathHelper.floor(h + currentX), n);

                for (int xToPlace = l; xToPlace <= o; xToPlace++) {
                    double s = ((double) xToPlace + 0.5 - currentY) / currentX;
                    if (s * s >= 1) {
                        continue;
                    }
                    for (int yToPlace = m; yToPlace <= p; yToPlace++) {
                        double u = (yToPlace - currentZ + 0.5) / currentX;
                        if (s * s + u * u >= 1) {
                            continue;
                        }
                        for (int zToPlace = n; zToPlace <= q; zToPlace++) {
                            double w = (zToPlace - h + 0.5) / currentX;
                            int aa = xToPlace - origin.getX() + (yToPlace - origin.getY()) * horizontalSize + (zToPlace - origin.getZ()) * horizontalSize * verticalSize;
                            if (s * s + u * u + w * w >= 1 || worldAccess.isOutOfHeightLimit(yToPlace) || bitSet.get(aa)) {
                                continue;
                            }
                            bitSet.set(aa);
                            BlockPos blockPos = new BlockPos(xToPlace, yToPlace, zToPlace);
                            if (worldAccess.isValidForSetBlock(blockPos)) {
                                ChunkSection chunkSection = chunkSectionCache.getSection(blockPos);
                                if (chunkSection != WorldChunk.EMPTY_SECTION && shouldPlace(blockPos)) {
                                    place(chunkSection, blockPos, oreVeinInstance);
                                    blocksPlaced++;
                                }
                            }
                        }
                    }
                }
            }
        } catch (Throwable e) {
            chunkSectionCache.close();
            throw e;
        }

        chunkSectionCache.close();
        return blocksPlaced > 0;
    }

    private boolean shouldPlace(BlockPos pos) {
        return !blockIsFloating(pos);
    }

    private void place(ChunkSection chunkSection, BlockPos blockPos, OreVeinInstance oreVeinInstance) {
        BlockState state = selectBlockState(oreVeinInstance.veinType);
        if (state == null) {
            return;
        }

        int localX = ChunkSectionPos.getLocalCoord(blockPos.getX());
        int localY = ChunkSectionPos.getLocalCoord(blockPos.getX());
        int localZ = ChunkSectionPos.getLocalCoord(blockPos.getZ());

        Block currentBlock = chunkSection.getBlockState(localX, localY, localZ).getBlock();
        if (!oreVeinInstance.veinType.replaceableBlocks.contains(currentBlock)) {
            return;
        }

        chunkSection.setBlockState(localX, localY, localZ, state, false);
        addToVeinMap(blockPos, oreVeinInstance);
        oreVeinInstance.blocks.add(blockPos);
    }

    public void addToVeinMap(BlockPos blockPos, OreVeinInstance oreVeinInstance) {
        if (veinMap == null) {
            veinMap = new HashMap<>();
        }

        veinMap.put(blockPos.toShortString(), oreVeinInstance);
    }

    public OreVein selectOreVein(int y) {
        if (Math.random() < BetterOreGenConfig.getBaseFrequency()) {
            return null;
        }

        ArrayList<OreVein> possibleOreVeins = new ArrayList<>();
        double frequencySum = 0;
        for (OreVein oreVein : BetterOreGen.oreVeins) {
            if (y > oreVein.yMin && y < oreVein.yMax) {
                possibleOreVeins.add(oreVein);
                frequencySum += oreVein.frequency;
            }
        }

        if (possibleOreVeins.size() < 1) {
            return null;
        }

        double rand = Math.random() * frequencySum;
        int selectionIndex = 0;
        frequencySum = possibleOreVeins.get(selectionIndex).frequency;
        while (frequencySum < rand) {
            selectionIndex++;
            frequencySum += possibleOreVeins.get(selectionIndex).frequency;
        }

        return possibleOreVeins.get(selectionIndex);
    }

    public BlockState selectBlockState(OreVein oreVein) {
        if (Math.random() > oreVein.density) {
            return null;
        }

        int randInt = (int) (Math.random() * 100);
        int selectionIndex = 0;
        int weightsSum = oreVein.blockWeights.get(selectionIndex);

        while (weightsSum < randInt) {
            selectionIndex++;
            weightsSum += oreVein.blockWeights.get(selectionIndex);
        }

        return oreVein.blocks.get(selectionIndex).getDefaultState();
    }

    private boolean blockIsFloating(OreVeinBlock oreVeinBlock) {
        for (Direction direction : Direction.values()) {
            if (oreVeinBlock.blockState.isSideSolid(worldAccess, oreVeinBlock.blockPos, direction, SideShapeType.FULL)) {
                return false;
            }
        }
        return true;
    }

    private boolean blockIsFloating(BlockPos blockPos) {
        return blockIsFloating(new OreVeinBlock(blockPos));
    }

    private class OreVeinBlock {
        public BlockState blockState;
        public BlockPos blockPos;

        public OreVeinBlock(BlockPos blockPos) {
            this.blockPos = blockPos;
            this.blockState = worldAccess.getBlockState(blockPos);
        }
    }
}