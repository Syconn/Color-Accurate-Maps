package mod.syconn.accuratemaps.platform.forge.networking;

import mod.syconn.accuratemaps.platform.forge.ForgeClientAccurateMaps;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record ReceiveMapPacket(int id, CompoundTag nbt) {

    public static void encode(ReceiveMapPacket pkt, FriendlyByteBuf buf) {
        buf.writeInt(pkt.id);
        buf.writeNbt(pkt.nbt);
    }

    public static ReceiveMapPacket decode(FriendlyByteBuf buf) {
        return new ReceiveMapPacket(buf.readInt(), buf.readNbt());
    }

    public static void handle(ReceiveMapPacket pkt, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ForgeClientAccurateMaps.onReceiveMap(pkt.id, pkt.nbt));
        context.setPacketHandled(true);
    }
}