package mod.syconn.accuratemaps.mixin.client;

import mod.syconn.accuratemaps.client.ClientAccurateMaps;
import net.minecraft.client.gui.MapRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MapRenderer.MapInstance.class)
public class MapInstanceMixin {

    @Shadow
    private MapItemSavedData data;

    @Shadow @Final
    private DynamicTexture texture;

    @Unique
    private int id;

    @Inject(at = @At("TAIL"), method = "<init>")
    public void afterInit(MapRenderer mapRenderer, int i, MapItemSavedData mapItemSavedData, CallbackInfo ci) {
        this.id = i;
    }

    @Inject(at = @At("HEAD"), method = "updateTexture", cancellable = true)
    public void updateAccurateTexture(CallbackInfo ci) {
//        var additionalData = ClientAccurateMaps.getMapData(id);
//        if(additionalData != null) {
//            ClientAccurateMaps.updateTexture(texture, data, additionalData);
//            ci.cancel();
//        }
    }

}
