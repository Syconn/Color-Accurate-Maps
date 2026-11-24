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

//? if >=26.1 {

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.resources.Identifier;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.saveddata.SavedDataType;

import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
//? }

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.saveddata.SavedData;
//? if 1.21.1
//import net.minecraft.core.HolderLookup;
//? if 1.20.1
//import org.jetbrains.annotations.NotNull;

public class AdditionalMapData extends SavedData {

    private long[] positions = new long[16384];
    private int[] states = new int[16384];

    //? if >=26.1 {
    public static SavedDataType<AdditionalMapData> type(MapId id) {
        return new SavedDataType<>(
                Identifier.fromNamespaceAndPath("accuratemaps", "accurate_map_" + id.key()),
                AdditionalMapData::new,
                RecordCodecBuilder.create(instance -> instance.group(
                        Codec.LONG_STREAM.xmap(LongStream::toArray, Arrays::stream).fieldOf("positions").forGetter(data -> data.positions),
                        Codec.INT_STREAM.xmap(IntStream::toArray, Arrays::stream).fieldOf("states").forGetter(data -> data.states)
                ).apply(instance, AdditionalMapData::new)),
                DataFixTypes.SAVED_DATA_FORCED_CHUNKS
        );
    }
    //? }

    public AdditionalMapData() {
    }


    //? if <26.1 {
    /*public AdditionalMapData(CompoundTag tag) {
        positions = tag.getLongArray("Pos");
        states = tag.getIntArray("States");
    }

    public static AdditionalMapData clientRead(CompoundTag tag) {
        return new AdditionalMapData(tag);
    }
    *///? } else {
    public AdditionalMapData(long[] positions, int[] states) {
        this.positions = positions;
        this.states = states;
    }

    public static AdditionalMapData clientRead(CompoundTag tag) {
        var positions = tag.getLongArray("Pos").orElse(new long[16384]);
        var states = tag.getIntArray("States").orElse(new int[16384]);
        return new AdditionalMapData(positions, states);
    }
    //? }

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
    /*@Override
    public @NotNull CompoundTag save(CompoundTag compoundTag) {
        compoundTag.putIntArray("States", states);
        compoundTag.putLongArray("Pos", positions);
        return compoundTag;
    }
    *///? } else if <26.1 {
    /*@Override
    public @NotNull CompoundTag save(CompoundTag compoundTag, HolderLookup.Provider provider) {
        return clientSave(compoundTag);
    }
    *///? }
}
