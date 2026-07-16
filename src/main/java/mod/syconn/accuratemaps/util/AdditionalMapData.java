package mod.syconn.accuratemaps.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;

public class AdditionalMapData extends SavedData {

//    private List<Integer> tintColor = new IntArrayList();
    private int[] states = new int[16384];

    public AdditionalMapData() {}

    public AdditionalMapData(CompoundTag tag) {
        states = tag.getIntArray("States");
    }

    public void setBlock(int x, int z, MapColor color) {
        if (color instanceof AdditionalMapColor c) {
            states[x + z * 128] = c.getBlockState();
        }
    }

    public int[] getStates() {
        return states;
    }

    @Override
    public @NotNull CompoundTag save(CompoundTag compoundTag) {
        compoundTag.putIntArray("States", states);
        return compoundTag;
    }
}
