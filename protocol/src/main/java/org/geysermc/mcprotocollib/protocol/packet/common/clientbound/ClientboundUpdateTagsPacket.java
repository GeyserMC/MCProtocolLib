package org.geysermc.mcprotocollib.protocol.packet.common.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import net.kyori.adventure.key.Key;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;

import java.util.HashMap;
import java.util.Map;

@Data
@With
@AllArgsConstructor
public class ClientboundUpdateTagsPacket implements MinecraftPacket {
    private final @NonNull Map<Key, Map<Key, int[]>> tags = new HashMap<>();

    public ClientboundUpdateTagsPacket(ByteBuf in) {
        int totalTagCount = MinecraftTypes.readVarInt(in);
        for (int i = 0; i < totalTagCount; i++) {
            Map<Key, int[]> tag = new HashMap<>();
            Key tagName = MinecraftTypes.readResourceLocation(in);
            int tagsCount = MinecraftTypes.readVarInt(in);
            for (int j = 0; j < tagsCount; j++) {
                Key name = MinecraftTypes.readResourceLocation(in);
                int entriesCount = MinecraftTypes.readVarInt(in);
                int[] entries = new int[entriesCount];
                for (int index = 0; index < entriesCount; index++) {
                    entries[index] = MinecraftTypes.readVarInt(in);
                }

                tag.put(name, entries);
            }
            tags.put(tagName, tag);
        }
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeVarInt(out, tags.size());
        for (Map.Entry<Key, Map<Key, int[]>> tagSet : tags.entrySet()) {
            MinecraftTypes.writeResourceLocation(out, tagSet.getKey());
            MinecraftTypes.writeVarInt(out, tagSet.getValue().size());
            for (Map.Entry<Key, int[]> tag : tagSet.getValue().entrySet()) {
                MinecraftTypes.writeResourceLocation(out, tag.getKey());
                MinecraftTypes.writeVarInt(out, tag.getValue().length);
                for (int id : tag.getValue()) {
                    MinecraftTypes.writeVarInt(out, id);
                }
            }
        }
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
