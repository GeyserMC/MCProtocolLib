package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound;

import io.netty.buffer.ByteBuf;
import lombok.Data;
import lombok.With;
import net.kyori.adventure.key.Key;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;

import java.util.HashMap;
import java.util.Map;

@Data
@With
public class ServerboundSetGameRulePacket implements MinecraftPacket {
    private final Map<Key, String> entries;

    public ServerboundSetGameRulePacket(Map<Key, String> entries) {
        this.entries = Map.copyOf(entries);
    }

    public ServerboundSetGameRulePacket(ByteBuf in) {
        this.entries = new HashMap<>();
        int size = MinecraftTypes.readVarInt(in);
        for (int i = 0; i < size; i++) {
            this.entries.put(MinecraftTypes.readResourceLocation(in), MinecraftTypes.readString(in));
        }
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeVarInt(out, this.entries.size());
        for (Map.Entry<Key, String> entry : this.entries.entrySet()) {
            MinecraftTypes.writeResourceLocation(out, entry.getKey());
            MinecraftTypes.writeString(out, entry.getValue());
        }
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
