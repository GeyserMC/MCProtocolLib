package com.github.steveice10.mc.protocol.packet.ingame.clientbound;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import com.github.steveice10.mc.protocol.data.game.Identifier;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;

import java.io.IOException;
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
        helper.writeOnNestedTagSet(out,tags);
    }
}
