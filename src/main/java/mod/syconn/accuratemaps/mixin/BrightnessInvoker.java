package mod.syconn.accuratemaps.mixin;

import net.minecraft.world.level.material.MapColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(MapColor.Brightness.class)
public interface BrightnessInvoker {

    @Invoker
    static MapColor.Brightness invokeByIdUnsafe(int id) {
        throw new AssertionError();
    }
}
