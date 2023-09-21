package com.github.steveice10.mc.protocol.packet.common.clientbound;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;

import java.util.HashMap;
import java.util.Map;

@Data
@With
@AllArgsConstructor
public class ClientboundUpdateTagsPacket implements MinecraftPacket {
    private final @NonNull Map<String, Map<String, int[]>> tags = new HashMap<>();

    public ClientboundUpdateTagsPacket(ByteBuf in, MinecraftCodecHelper helper) {
        int totalTagCount = helper.readVarInt(in);
        for (int i = 0; i < totalTagCount; i++) {
            Map<String, int[]> tag = new HashMap<>();
            String tagName = helper.readResourceLocation(in);
            int tagsCount = helper.readVarInt(in);
            for (int j = 0; j < tagsCount; j++) {
                String name = helper.readResourceLocation(in);
                int entriesCount = helper.readVarInt(in);
                int[] entries = new int[entriesCount];
                for (int index = 0; index < entriesCount; index++) {
                    entries[index] = helper.readVarInt(in);
                }

                tag.put(name, entries);
            }
            tags.put(tagName, tag);
        }
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writeVarInt(out, tags.size());
        for (Map.Entry<String, Map<String, int[]>> tagSet : tags.entrySet()) {
            helper.writeResourceLocation(out, tagSet.getKey());
            helper.writeVarInt(out, tagSet.getValue().size());
            for (Map.Entry<String, int[]> tag : tagSet.getValue().entrySet()) {
                helper.writeResourceLocation(out, tag.getKey());
                helper.writeVarInt(out, tag.getValue().length);
                for (int id : tag.getValue()) {
                    helper.writeVarInt(out, id);
                }
            }
        }
    }
}
