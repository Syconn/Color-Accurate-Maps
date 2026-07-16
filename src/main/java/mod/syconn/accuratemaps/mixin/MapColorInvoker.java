package mod.syconn.accuratemaps.mixin;

import net.minecraft.world.level.material.MapColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(MapColor.class)
public interface MapColorInvoker {

    @Invoker
    static MapColor invokeByIdUnsafe(int id) {
        throw new AssertionError();
    }
}
