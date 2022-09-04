package me.sammc19.betteroregen.mixin;

import java.util.BitSet;
import java.util.Random;
import java.util.function.Function;
import me.sammc19.betteroregen.BetterOreGen;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.ChunkSectionCache;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.WorldChunk;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.util.FeatureContext;
import org.spongepowered.asm.mixin.Mixin;


@Mixin(OreFeature.class)
public abstract class OreFeatureMixin {

    private static boolean shouldPlaceBlock(BlockState state, Function<BlockPos, BlockState> posToState, Random random, OreFeatureConfig config, OreFeatureConfig.Target target, BlockPos.Mutable pos) {
        if (!target.target.test(state, random)) {
            return false;
        }
        if (shouldNotDiscardPlacement(random, config.discardOnAirChance)) {
            return true;
        }
        return !Feature.isExposedToAir(posToState, pos);
    }

    private static boolean shouldNotDiscardPlacement(Random random, float chance) {
        if (chance <= 0.0f) {
            return true;
        }
        if (chance >= 1.0f) {
            return false;
        }
        return random.nextFloat() >= chance;
    }

    // TODO: Find a better way of doing this than just copying the whole method
    // Using @Inject resulted in org.spongepowered.asm.mixin.injection.callback.CancellationException: The call generate is not cancellable.
    public boolean generate(FeatureContext<OreFeatureConfig> context) {
        // TODO: Find a less hacky way to determine which features are BETTER_ORE_GEN_CONFIGURED_FEATUREs
        if (context.getConfig().size == 0) {
            return BetterOreGen.veinGenerator.generateVein(context);
        }

        Random random = context.getRandom();
        BlockPos blockPos = context.getOrigin();
        StructureWorldAccess structureWorldAccess = context.getWorld();
        OreFeatureConfig oreFeatureConfig = context.getConfig();
        float f = random.nextFloat() * (float)Math.PI;
        float g = (float)oreFeatureConfig.size / 8.0f;
        int i = MathHelper.ceil(((float)oreFeatureConfig.size / 16.0f * 2.0f + 1.0f) / 2.0f);
        double d = (double)blockPos.getX() + Math.sin(f) * (double)g;
        double e = (double)blockPos.getX() - Math.sin(f) * (double)g;
        double h = (double)blockPos.getZ() + Math.cos(f) * (double)g;
        double j = (double)blockPos.getZ() - Math.cos(f) * (double)g;
        int k = 2;
        double l = blockPos.getY() + random.nextInt(3) - 2;
        double m = blockPos.getY() + random.nextInt(3) - 2;
        int n = blockPos.getX() - MathHelper.ceil(g) - i;
        int o = blockPos.getY() - 2 - i;
        int p = blockPos.getZ() - MathHelper.ceil(g) - i;
        int q = 2 * (MathHelper.ceil(g) + i);
        int r = 2 * (2 + i);
        for (int s = n; s <= n + q; ++s) {
            for (int t = p; t <= p + q; ++t) {
                if (o > structureWorldAccess.getTopY(Heightmap.Type.OCEAN_FLOOR_WG, s, t)) continue;
                return this.generateVeinSection(structureWorldAccess, random, oreFeatureConfig, d, e, h, j, l, m, n, o, p, q, r);
            }
        }
        return false;
    }

    private boolean generateVeinSection(StructureWorldAccess structureWorldAccess, Random random, OreFeatureConfig config, double startX, double endX, double startZ, double endZ, double startY, double endY, int x, int y, int z, int horizontalSize, int verticalSize) {
        double h;
        double g;
        double e;
        double d;
        int k;
        int i = 0;
        BitSet bitSet = new BitSet(horizontalSize * verticalSize * horizontalSize);
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        int j = config.size;
        double[] ds = new double[j * 4];
        for (k = 0; k < j; ++k) {
            float f = (float) k / (float) j;
            d = MathHelper.lerp((double) f, startX, endX);
            e = MathHelper.lerp((double) f, startY, endY);
            g = MathHelper.lerp((double) f, startZ, endZ);
            h = random.nextDouble() * (double) j / 16.0;
            double l = ((double) (MathHelper.sin((float) Math.PI * f) + 1.0f) * h + 1.0) / 2.0;
            ds[k * 4 + 0] = d;
            ds[k * 4 + 1] = e;
            ds[k * 4 + 2] = g;
            ds[k * 4 + 3] = l;
        }
        for (k = 0; k < j - 1; ++k) {
            if (ds[k * 4 + 3] <= 0.0) continue;
            for (int f = k + 1; f < j; ++f) {
                if (ds[f * 4 + 3] <= 0.0 || !((h = ds[k * 4 + 3] - ds[f * 4 + 3]) * h > (d = ds[k * 4 + 0] - ds[f * 4 + 0]) * d + (e = ds[k * 4 + 1] - ds[f * 4 + 1]) * e + (g = ds[k * 4 + 2] - ds[f * 4 + 2]) * g))
                    continue;
                if (h > 0.0) {
                    ds[f * 4 + 3] = -1.0;
                    continue;
                }
                ds[k * 4 + 3] = -1.0;
            }
        }
        try (ChunkSectionCache k2 = new ChunkSectionCache(structureWorldAccess);) {
            for (int f = 0; f < j; ++f) {
                d = ds[f * 4 + 3];
                if (d < 0.0) continue;
                e = ds[f * 4 + 0];
                g = ds[f * 4 + 1];
                h = ds[f * 4 + 2];
                int l = Math.max(MathHelper.floor(e - d), x);
                int m = Math.max(MathHelper.floor(g - d), y);
                int n = Math.max(MathHelper.floor(h - d), z);
                int o = Math.max(MathHelper.floor(e + d), l);
                int p = Math.max(MathHelper.floor(g + d), m);
                int q = Math.max(MathHelper.floor(h + d), n);
                for (int r = l; r <= o; ++r) {
                    double s = ((double) r + 0.5 - e) / d;
                    if (!(s * s < 1.0)) continue;
                    for (int t = m; t <= p; ++t) {
                        double u = ((double) t + 0.5 - g) / d;
                        if (!(s * s + u * u < 1.0)) continue;
                        block11:
                        for (int v = n; v <= q; ++v) {
                            ChunkSection chunkSection;
                            int aa;
                            double w = ((double) v + 0.5 - h) / d;
                            if (!(s * s + u * u + w * w < 1.0) || structureWorldAccess.isOutOfHeightLimit(t) || bitSet.get(aa = r - x + (t - y) * horizontalSize + (v - z) * horizontalSize * verticalSize))
                                continue;
                            bitSet.set(aa);
                            mutable.set(r, t, v);
                            if (!structureWorldAccess.isValidForSetBlock(mutable) || (chunkSection = k2.getSection(mutable)) == WorldChunk.EMPTY_SECTION)
                                continue;
                            int ab = ChunkSectionPos.getLocalCoord(r);
                            int ac = ChunkSectionPos.getLocalCoord(t);
                            int ad = ChunkSectionPos.getLocalCoord(v);
                            BlockState blockState = chunkSection.getBlockState(ab, ac, ad);
                            for (OreFeatureConfig.Target target : config.targets) {
                                if (!shouldPlaceBlock(blockState, k2::getBlockState, random, config, target, mutable))
                                    continue;
                                chunkSection.setBlockState(ab, ac, ad, target.state, false);
                                ++i;
                                continue block11;
                            }
                        }
                    }
                }
            }
        }
        return i > 0;
    }
}
