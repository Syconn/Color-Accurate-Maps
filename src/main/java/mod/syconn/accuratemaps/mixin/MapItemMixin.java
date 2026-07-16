package mod.syconn.accuratemaps.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import mod.syconn.accuratemaps.util.AdditionalMapData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MapItem.class)
public class MapItemMixin {

    @Unique
    private AdditionalMapData additionalMapData;

    @Inject(method = "inventoryTick", at = @At("HEAD"))
    private void loadAdditionalMapData(ItemStack itemStack, Level level, Entity entity, int i, boolean bl, CallbackInfo ci) {
        if (level instanceof ServerLevel serverLevel) {
            Integer id = MapItem.getMapId(itemStack);
            if(id != null) additionalMapData = serverLevel.getServer().overworld().getDataStorage().computeIfAbsent(AdditionalMapData::new, AdditionalMapData::new, "accurate_map_"+id);
        }
    }

//    @Inject(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/saveddata/maps/MapItemSavedData;updateColor(IIB)Z", shift = At.Shift.AFTER))
//    private void updateAdditionalMap(Level level, Entity entity, MapItemSavedData mapItemSavedData, CallbackInfo ci, @Local int o, @Local int p, @Local(name = "mapColor") MapColor mapColor) {
//
//    }

    // TODO IF INCORRECT PULL WITH PRINT LOCALS
    @WrapOperation(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/saveddata/maps/MapItemSavedData;updateColor(IIB)Z"))
    private boolean onUpdateColor(MapItemSavedData data, int i, int j, byte b, Operation<Boolean> original, @Local(name = "mapColor") MapColor mapColor) {
        additionalMapData.setBlock(i, j, mapColor);
        return original.call(data, i, j, b);
    }
}
