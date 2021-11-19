package me.sammc19.betteroregen.mixin;

import me.sammc19.betteroregen.generation.VeinGenerator;
import net.minecraft.world.gen.feature.OreFeature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.util.FeatureContext;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(OreFeature.class)
public abstract class OreFeatureMixin {

    private final VeinGenerator veinGenerator = new VeinGenerator();

    public boolean generate(FeatureContext<OreFeatureConfig> context) {
        return veinGenerator.generate(context);
    }
}
