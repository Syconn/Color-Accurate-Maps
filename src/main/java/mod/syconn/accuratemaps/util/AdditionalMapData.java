package mod.syconn.accuratemaps.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;

public class AdditionalMapData extends SavedData {

    private long[] positions = new long[16384];
    private int[] states = new int[16384];

    public AdditionalMapData() {}

    public AdditionalMapData(CompoundTag tag) {
        positions = tag.getLongArray("Pos");
        states = tag.getIntArray("States");
    }

    public void setBlock(int x, int z, MapColor color) {
        if (color instanceof AdditionalMapColor c) {
            positions[x + z * 128] = c.getPos().asLong();
            states[x + z * 128] = c.getBlockState();
        }
    }

    public int[] getStates() {
        return states;
    }

    public long[] getPositions() {
        return positions;
    }

    @Override
    public @NotNull CompoundTag save(CompoundTag compoundTag) {
        compoundTag.putIntArray("States", states);
        compoundTag.putLongArray("Pos", positions);
        return compoundTag;
    }
}
