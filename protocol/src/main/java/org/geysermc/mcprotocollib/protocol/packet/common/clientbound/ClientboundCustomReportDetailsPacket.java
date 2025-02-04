package org.geysermc.mcprotocollib.protocol.packet.common.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;

import java.util.HashMap;
import java.util.Map;

@Data
@With
@AllArgsConstructor
public class ClientboundCustomReportDetailsPacket implements MinecraftPacket {
    private final Map<String, String> details;

    public ClientboundCustomReportDetailsPacket(ByteBuf in) {
        this.details = new HashMap<>();

        int count = MinecraftTypes.readVarInt(in);
        for (int i = 0; i < count; i++) {
            this.details.put(MinecraftTypes.readString(in, 128), MinecraftTypes.readString(in, 4096));
        }
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeVarInt(out, this.details.size());
        for (Map.Entry<String, String> entry : this.details.entrySet()) {
            MinecraftTypes.writeString(out, entry.getKey());
            MinecraftTypes.writeString(out, entry.getValue());
        }
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
