/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * Based on Accurate Maps by D4rkness_King / lucaargolo
 * (https://github.com/lucaargolo/accurate-maps), MPL-2.0.
 * Rewritten in Java for Minecraft 1.20+ using Stonecutter.
 */

package mod.syconn.accuratemaps.platform.neo;

//? if neoforge {
/*import mod.syconn.accuratemaps.client.ClientAccurateMaps;
import mod.syconn.accuratemaps.util.AdditionalMapData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundMapItemDataPacket;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

@Mod(value = NeoAccurateMaps.MOD, dist = Dist.CLIENT)
public class NeoClientAccurateMaps {

    public NeoClientAccurateMaps(IEventBus modBus) {
        NeoForge.EVENT_BUS.addListener(NeoClientAccurateMaps::onJoin);
    }

    public static void onMapUpdate(ClientboundMapItemDataPacket packet) {
        ClientPacketListener connection = Minecraft.getInstance().getConnection();
        if (connection != null && connection.hasChannel(NeoAccurateMaps.RequestMap.TYPE))
            PacketDistributor.sendToServer(new NeoAccurateMaps.RequestMap(packet.mapId()));
    }

    private static void onJoin(ClientPlayerNetworkEvent.LoggingIn event) {
        Minecraft client = Minecraft.getInstance();
        if (ClientAccurateMaps.isStateColorMapEmpty()) ClientAccurateMaps.paintBlockColorMap(client);
        ClientAccurateMaps.clearMapData();
    }

    public static void handleReceiveMap(NeoAccurateMaps.ReceiveMap payload, IPayloadContext context) {
        ClientAccurateMaps.putMapData(payload.id().id(), new AdditionalMapData(payload.nbt()));
    }
}
*///? }