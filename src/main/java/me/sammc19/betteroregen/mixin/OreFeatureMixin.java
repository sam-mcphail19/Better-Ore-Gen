package me.sammc19.betteroregen.mixin;

import static me.sammc19.betteroregen.BetterOreGen.LOGGER;

import net.minecraft.world.gen.feature.OreFeature;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(OreFeature.class)
public abstract class OreFeatureMixin {
    /*
    @Redirect(method = "generateVeinPart()Z",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/WorldAccess;" +
                            "setBlockState(" +
                            "Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I" +
                            ")Z"))
    public boolean removeOreFeatures(WorldAccess world, BlockPos mutable, BlockState state, int flags) {
        //LOGGER.info();
        state = BetterOreGen.oreRemover.removeOre(world, mutable, state);
        return world.setBlockState(mutable, state, flags);
    }
    */

    /*
        This removes all ore features including granite, diorite, dirt, etc.
        and also any ore feature i add lol


    @Inject(method="generate()Z", at=@At("HEAD"), cancellable=true)
    public void removeOreFeatures(CallbackInfoReturnable<Boolean> cir){
        //cir.setReturnValue(false);
    }
*/
}
