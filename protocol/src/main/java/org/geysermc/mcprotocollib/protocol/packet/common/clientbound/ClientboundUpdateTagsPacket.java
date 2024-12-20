package org.geysermc.mcprotocollib.protocol.packet.common.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import net.kyori.adventure.key.Key;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

import java.util.HashMap;
import java.util.Map;

@Data
@With
@AllArgsConstructor
public class ClientboundUpdateTagsPacket implements MinecraftPacket {
    private final @NonNull Map<Key, Map<Key, int[]>> tags = new HashMap<>();

    public ClientboundUpdateTagsPacket(ByteBuf in, MinecraftCodecHelper helper) {
        int totalTagCount = helper.readVarInt(in);
        for (int i = 0; i < totalTagCount; i++) {
            Map<Key, int[]> tag = new HashMap<>();
            Key tagName = helper.readResourceLocation(in);
            int tagsCount = helper.readVarInt(in);
            for (int j = 0; j < tagsCount; j++) {
                Key name = helper.readResourceLocation(in);
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
        for (Map.Entry<Key, Map<Key, int[]>> tagSet : tags.entrySet()) {
            helper.writeResourceLocation(out, tagSet.getKey());
            helper.writeVarInt(out, tagSet.getValue().size());
            for (Map.Entry<Key, int[]> tag : tagSet.getValue().entrySet()) {
                helper.writeResourceLocation(out, tag.getKey());
                helper.writeVarInt(out, tag.getValue().length);
                for (int id : tag.getValue()) {
                    helper.writeVarInt(out, id);
                }
            }
        }
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
