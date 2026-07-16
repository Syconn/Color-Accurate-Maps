package mod.syconn.accuratemaps.mixin;

import mod.syconn.accuratemaps.util.AdditionalMapColor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.BlockStateBase.class)
public class BlockStateMixin {

    @Inject(method = "getMapColor", at = @At("RETURN"), cancellable = true)
    public void overrideMapColor(BlockGetter blockGetter, BlockPos blockPos, CallbackInfoReturnable<MapColor> cir) {
        var originalColor = cir.getReturnValue();
        var state = blockGetter.getBlockState(blockPos);
        cir.setReturnValue(new AdditionalMapColor(originalColor, state, blockPos));
    }
}
