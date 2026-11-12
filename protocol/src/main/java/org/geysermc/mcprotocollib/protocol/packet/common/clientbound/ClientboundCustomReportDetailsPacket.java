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
        if (count < 0 || count > 32) {
            throw new IllegalArgumentException("Report exceeds maximum of 32 details");
        }
        for (int i = 0; i < count; i++) {
            this.details.put(MinecraftTypes.readString(in, 128), MinecraftTypes.readString(in, 4096));
        }
    }

    @Override
    public void serialize(ByteBuf out) {
        if (this.details.size() > 32) {
            throw new IllegalArgumentException("Report exceeds maximum of 32 details");
        }
        MinecraftTypes.writeVarInt(out, this.details.size());
        for (Map.Entry<String, String> entry : this.details.entrySet()) {
            MinecraftTypes.writeString(out, entry.getKey(), 128);
            MinecraftTypes.writeString(out, entry.getValue(), 4096);
        }
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
