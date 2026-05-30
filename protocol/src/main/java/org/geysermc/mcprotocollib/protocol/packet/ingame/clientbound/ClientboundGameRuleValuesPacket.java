package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound;

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
public class ClientboundGameRuleValuesPacket implements MinecraftPacket {
    private final Map<Key, String> values;

    public ClientboundGameRuleValuesPacket(Map<Key, String> values) {
        this.values = Map.copyOf(values);
    }

    public ClientboundGameRuleValuesPacket(ByteBuf in) {
        this.values = new HashMap<>();
        int size = MinecraftTypes.readVarInt(in);
        for (int i = 0; i < size; i++) {
            this.values.put(MinecraftTypes.readResourceLocation(in), MinecraftTypes.readString(in));
        }
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeVarInt(out, this.values.size());
        for (Map.Entry<Key, String> entry : this.values.entrySet()) {
            MinecraftTypes.writeResourceLocation(out, entry.getKey());
            MinecraftTypes.writeString(out, entry.getValue());
        }
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
