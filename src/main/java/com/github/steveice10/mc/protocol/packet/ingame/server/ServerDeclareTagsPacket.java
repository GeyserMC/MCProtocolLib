package com.github.steveice10.mc.protocol.packet.ingame.server;

import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Data
@Setter(AccessLevel.NONE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ServerDeclareTagsPacket implements Packet {
    private @NonNull Map<String, int[]> blockTags;
    private @NonNull Map<String, int[]> itemTags;
    private @NonNull Map<String, int[]> fluidTags;
    private @NonNull Map<String, int[]> entityTags;

    @Override
    public void read(NetInput in) throws IOException {
        this.blockTags = new HashMap<>();
        this.itemTags = new HashMap<>();
        this.fluidTags = new HashMap<>();
        this.entityTags = new HashMap<>();
        for(Map<String, int[]> tags : Arrays.asList(this.blockTags, this.itemTags, this.fluidTags, this.entityTags)) {
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
        for(Map<String, int[]> tags : Arrays.asList(this.blockTags, this.itemTags, this.fluidTags, this.entityTags)) {
            out.writeVarInt(tags.size());
            for(Map.Entry<String, int[]> tag : tags.entrySet()) {
                out.writeString(tag.getKey());
                out.writeVarInt(tag.getValue().length);
                for(int id : tag.getValue()) {
                    out.writeVarInt(id);
                }
            }
        }
    }

    @Override
    public boolean isPriority() {
        return false;
    }
}
