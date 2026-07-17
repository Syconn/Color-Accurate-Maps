package mod.syconn.accuratemaps.mixin;

import com.google.common.collect.Multiset;
import mod.syconn.accuratemaps.util.AdditionalMapData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(MapItem.class)
public class MapItemMixin {

    @Unique
    private AdditionalMapData additionalMapData;

    @Inject(method = "inventoryTick", at = @At("HEAD"))
    private void loadAdditionalMapData(ItemStack itemStack, Level level, Entity entity, int i, boolean bl, CallbackInfo ci) {
        if (level instanceof ServerLevel serverLevel) {
            //? if 1.20.1 {
            /*//var id = MapItem.getMapId(itemStack);
            if(id != null) additionalMapData = serverLevel.getServer().overworld().getDataStorage().computeIfAbsent(AdditionalMapData::new, AdditionalMapData::new, "accurate_map_"+id);
            *///? } else {
            var id = itemStack.get(DataComponents.MAP_ID);
            if (id != null) additionalMapData = serverLevel.getServer().overworld().getDataStorage().computeIfAbsent(new SavedData.Factory<>(AdditionalMapData::new, AdditionalMapData::new, DataFixTypes.SAVED_DATA_FORCED_CHUNKS), "accurate_map_"+id.key());
            //? }
        }
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/saveddata/maps/MapItemSavedData;updateColor(IIB)Z"), method = "update", locals = LocalCapture.CAPTURE_FAILHARD)
    public void updateAccurateMapState(Level level, Entity entity, MapItemSavedData mapItemSavedData, CallbackInfo ci, int i, int j, int k, int l, int m, int n, MapItemSavedData.HoldingPlayer holdingPlayer, BlockPos.MutableBlockPos mutableBlockPos, BlockPos.MutableBlockPos mutableBlockPos2, boolean bl, int o, double d, int p, int q, boolean bl2, int r, int s, Multiset multiset, LevelChunk levelChunk, int t, double e, MapColor mapColor, MapColor.Brightness brightness) {
        additionalMapData.setBlock(o, p, mapColor);
    }
}
