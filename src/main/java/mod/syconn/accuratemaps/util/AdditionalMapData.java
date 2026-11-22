/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * Based on Accurate Maps by D4rkness_King / lucaargolo
 * (https://github.com/lucaargolo/accurate-maps), MPL-2.0.
 * Rewritten in Java for Minecraft 1.20+ using Stonecutter.
 */

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
