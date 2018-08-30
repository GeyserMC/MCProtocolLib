package com.github.steveice10.mc.protocol.packet.ingame.server;

import com.github.steveice10.mc.protocol.packet.MinecraftPacket;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ServerDeclareTagsPacket extends MinecraftPacket {
    private Map<String, int[]> blockTags;
    private Map<String, int[]> itemTags;
    private Map<String, int[]> fluidTags;

    @SuppressWarnings("unused")
    private ServerDeclareTagsPacket() {
    }

    public ServerDeclareTagsPacket(Map<String, int[]> blockTags, Map<String, int[]> itemTags, Map<String, int[]> fluidTags) {
        this.blockTags = blockTags;
        this.itemTags = itemTags;
        this.fluidTags = fluidTags;
    }

    public Map<String, int[]> getBlockTags() {
        return this.blockTags;
    }

    public Map<String, int[]> getItemTags() {
        return this.itemTags;
    }

    public Map<String, int[]> getFluidTags() {
        return this.fluidTags;
    }

    @Override
    public void read(NetInput in) throws IOException {
        blockTags = new HashMap<>();
        itemTags = new HashMap<>();
        fluidTags = new HashMap<>();
        for(Map<String, int[]> tags : Arrays.asList(blockTags, itemTags, fluidTags)) {
            int tagsCount = in.readVarInt();
            for(int i = 0; i < tagsCount; i++) {
                String name = in.readString();
                int entriesCount = in.readVarInt();
                int[] entries = new int[entriesCount];
                for(int index = 0; index < entriesCount; index++) {
                    entries[index] = in.readVarInt();
                }
                tags.put(name, entries);
            }
        }
    }

    @Override
    public void write(NetOutput out) throws IOException {
        for(Map<String, int[]> tags : Arrays.asList(blockTags, itemTags, fluidTags)) {
            out.writeVarInt(tags.size());
            for (Map.Entry<String, int[]> tag : tags.entrySet()) {
                out.writeString(tag.getKey());
                out.writeVarInt(tag.getValue().length);
                for (int id : tag.getValue()) {
                    out.writeVarInt(id);
                }
            }
        }
    }
}
