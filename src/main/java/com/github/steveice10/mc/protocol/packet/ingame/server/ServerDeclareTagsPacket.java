package com.github.steveice10.mc.protocol.packet.ingame.server;

import com.github.steveice10.mc.protocol.data.game.Identifier;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.With;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Data
@With
@Setter(AccessLevel.NONE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ServerDeclareTagsPacket implements Packet {
    private @NonNull Map<String, Map<String, int[]>> tags = new HashMap<>();

    @Override
    public void read(NetInput in) throws IOException {
        int totalTagCount = in.readVarInt();
        for (int i = 0; i < totalTagCount; i++) {
            Map<String, int[]> tag = new HashMap<>();
            String tagName = Identifier.formalize(in.readString());
            int tagsCount = in.readVarInt();
            for (int j = 0; j < tagsCount; j++) {
                String name = in.readString();
                int entriesCount = in.readVarInt();
                int[] entries = new int[entriesCount];
                for (int index = 0; index < entriesCount; index++) {
                    entries[index] = in.readVarInt();
                }

                tag.put(name, entries);
            }
            tags.put(tagName, tag);
        }
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(tags.size());
        for (Map.Entry<String, Map<String, int[]>> tagSet : tags.entrySet()) {
            out.writeString(tagSet.getKey());
            out.writeVarInt(tagSet.getValue().size());
            for (Map.Entry<String, int[]> tag : tagSet.getValue().entrySet()) {
                out.writeString(tag.getKey());
                out.writeVarInt(tag.getValue().length);
                for (int id : tag.getValue()) {
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
