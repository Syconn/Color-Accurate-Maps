package mod.syconn.accuratemaps;

import mod.syconn.accuratemaps.util.AdditionalMapData;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

public class AccurateMaps implements ModInitializer {

    public static ResourceLocation REQUEST_MAP = new ResourceLocation("accuratemaps", "request_color_accurate_map");
    public static ResourceLocation RECEIVE_MAP = new ResourceLocation("accuratemaps", "receive_color_accurate_map");

    @Override
    public void onInitialize() {
        ServerPlayNetworking.registerGlobalReceiver(REQUEST_MAP, ((server, player, handler, buf, responseSender) ->{
            var id = buf.readInt();
            var accurateMapState = server.overworld().getDataStorage().computeIfAbsent(AdditionalMapData::new, AdditionalMapData::new, "accurate_map_"+id);
            var nbt = accurateMapState.save(new CompoundTag());
            var innerBuf = PacketByteBufs.create();
            innerBuf.writeInt(id);
            innerBuf.writeNbt(nbt);
            ServerPlayNetworking.send(player, RECEIVE_MAP, innerBuf);
        }));
    }
}
