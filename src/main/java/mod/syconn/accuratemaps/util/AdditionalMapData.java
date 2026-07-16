package mod.syconn.accuratemaps.util;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AdditionalMapData extends SavedData {

//    private List<Integer> tintColor = new IntArrayList();
    private final List<Integer> states = new IntArrayList();

    public AdditionalMapData() {}

    public AdditionalMapData(CompoundTag tag) {
        states.clear();
        states.addAll(IntArrayList.wrap(tag.getIntArray("States")));
    }

    public void setBlock(int x, int z, MapColor color) {
        if (color instanceof AdditionalMapColor c) {
            states.set(x + z * 128, c.getBlockState());
        }
    }

    @Override
    public @NotNull CompoundTag save(CompoundTag compoundTag) {
        compoundTag.putIntArray("States", states);
        return compoundTag;
    }
}
