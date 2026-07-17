package mod.syconn.accuratemaps.util;

import net.minecraft.core.HolderLookup;
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

    public AdditionalMapData(CompoundTag tag, HolderLookup.Provider provider) {
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

    public CompoundTag clientSave(CompoundTag tag) {
        tag.putIntArray("States", states);
        tag.putLongArray("Pos", positions);
        return tag;
    }

    //? if 1.20.1 {
    @Override
    public @NotNull CompoundTag save(CompoundTag compoundTag) {
        compoundTag.putIntArray("States", states);
        compoundTag.putLongArray("Pos", positions);
        return compoundTag;
    }
    //? } else {
    /*@Override
    public @NotNull CompoundTag save(CompoundTag compoundTag, HolderLookup.Provider provider) {
        return clientSave(compoundTag);
    }
    *///? }
}
