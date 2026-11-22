package mod.syconn.accuratemaps.platform.forge;

import mod.syconn.accuratemaps.client.ClientAccurateMaps;
import mod.syconn.accuratemaps.platform.forge.networking.ForgeNetwork;
import mod.syconn.accuratemaps.platform.forge.networking.RequestMapPacket;
import mod.syconn.accuratemaps.util.AdditionalMapData;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundMapItemDataPacket;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Objects;

@Mod.EventBusSubscriber(modid = ForgeAccurateMaps.MOD, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ForgeClientAccurateMaps {

    public static void onMapUpdate(ClientboundMapItemDataPacket packet) {
        var listener = Minecraft.getInstance().getConnection();
        if (listener != null && ForgeNetwork.CHANNEL.isRemotePresent(listener.getConnection())) ForgeNetwork.sendToServer(new RequestMapPacket(packet.getMapId()));
    }

    @SubscribeEvent
    public static void onJoin(ClientPlayerNetworkEvent.LoggingIn event) {
        if (ClientAccurateMaps.isStateColorMapEmpty()) ClientAccurateMaps.paintBlockColorMap(Minecraft.getInstance());
        ClientAccurateMaps.clearMapData();
    }

    public static void onReceiveMap(int id, CompoundTag nbt) {
        var tag = Objects.requireNonNullElseGet(nbt, CompoundTag::new);
        ClientAccurateMaps.putMapData(id, new AdditionalMapData(tag));
    }
}
