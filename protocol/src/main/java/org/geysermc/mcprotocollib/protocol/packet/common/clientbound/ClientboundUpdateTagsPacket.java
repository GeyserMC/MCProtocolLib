package org.geysermc.mcprotocollib.protocol.packet.common.clientbound;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

import java.util.HashMap;
import java.util.Map;

@Data
@With
@AllArgsConstructor
public class ClientboundUpdateTagsPacket implements MinecraftPacket {
    private final @NonNull Map<String, Map<String, int[]>> tags = new HashMap<>();

    public ClientboundUpdateTagsPacket(MinecraftByteBuf buf) {
        int totalTagCount = buf.readVarInt();
        for (int i = 0; i < totalTagCount; i++) {
            Map<String, int[]> tag = new HashMap<>();
            String tagName = buf.readResourceLocation();
            int tagsCount = buf.readVarInt();
            for (int j = 0; j < tagsCount; j++) {
                String name = buf.readResourceLocation();
                int entriesCount = buf.readVarInt();
                int[] entries = new int[entriesCount];
                for (int index = 0; index < entriesCount; index++) {
                    entries[index] = buf.readVarInt();
                }

                tag.put(name, entries);
            }
            tags.put(tagName, tag);
        }
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeVarInt(tags.size());
        for (Map.Entry<String, Map<String, int[]>> tagSet : tags.entrySet()) {
            buf.writeResourceLocation(tagSet.getKey());
            buf.writeVarInt(tagSet.getValue().size());
            for (Map.Entry<String, int[]> tag : tagSet.getValue().entrySet()) {
                buf.writeResourceLocation(tag.getKey());
                buf.writeVarInt(tag.getValue().length);
                for (int id : tag.getValue()) {
                    buf.writeVarInt(id);
                }
            }
        }
    }
}
