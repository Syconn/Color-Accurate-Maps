package mod.syconn.accuratemaps.mixin;

import mod.syconn.accuratemaps.util.AdditionalMapColor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.BlockStateBase.class)
public class BlockStateMixin {

    @Inject(method = "getMapColor", at = @At("HEAD"))
    public void overrideMapColor(BlockGetter blockGetter, BlockPos blockPos, CallbackInfoReturnable<MapColor> cir) {
        MapColor originalColor = cir.getReturnValue();
        if(blockGetter instanceof BlockAndTintGetter worldView) {
            cir.setReturnValue(new AdditionalMapColor(originalColor, blockGetter.getBlockState(blockPos)));
        }
    }
}
