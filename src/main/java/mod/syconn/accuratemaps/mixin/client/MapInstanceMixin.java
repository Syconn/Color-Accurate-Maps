package mod.syconn.accuratemaps.mixin.client;

//? if <26.1
//import net.minecraft.client.gui.MapRenderer;
import mod.syconn.accuratemaps.client.ClientAccurateMaps;
//? if >=26.1
import net.minecraft.client.resources.MapTextureManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//? if <26.1
//@Mixin(MapRenderer.MapInstance.class)
//? if >=26.1
@Mixin(targets = "net.minecraft.client.resources.MapTextureManager$MapInstance")
public class MapInstanceMixin {

    //? if >=26.1 {
    @Shadow
    private boolean requiresUpload;
    //? }

    @Shadow
    private MapItemSavedData data;

    @Shadow @Final
    private DynamicTexture texture;

    @Unique
    private int id;

    //? <26.1 {
    /*@Inject(at = @At("TAIL"), method = "<init>")
    public void afterInit(MapRenderer mapRenderer, int i, MapItemSavedData mapItemSavedData, CallbackInfo ci) {
        this.id = i;
    }

    @Inject(at = @At("HEAD"), method = "updateTexture", cancellable = true)
    public void updateAccurateTexture(CallbackInfo ci) {
        var additionalData = ClientAccurateMaps.getMapData(id);
        if(additionalData != null) {
            ClientAccurateMaps.updateTexture(texture, data, additionalData);
            ci.cancel();
        }
    }
    *///? } else {
    @Inject(at = @At("TAIL"), method = "<init>")
    private void accuratemaps$afterInit(MapTextureManager manager, int id, MapItemSavedData data, CallbackInfo ci) {
        this.id = id;
    }

    @Inject(at = @At("HEAD"), method = "updateTextureIfNeeded", cancellable = true)
    private void accuratemaps$updateAccurateTexture(CallbackInfo ci) {
        if (!this.requiresUpload) return;

        var additionalData = ClientAccurateMaps.getMapData(this.id);
        if (additionalData != null) {
            ClientAccurateMaps.updateTexture(this.texture, this.data, additionalData);
            this.requiresUpload = false;
            ci.cancel();
        }
    }
    //? }
}
