package org.geysermc.mcprotocollib.protocol.packet.common.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

import java.util.HashMap;
import java.util.Map;

@Data
@With
@AllArgsConstructor
public class ClientboundCustomReportDetailsPacket implements MinecraftPacket {
    private final Map<String, String> details;

    public ClientboundCustomReportDetailsPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.details = new HashMap<>();

        int count = helper.readVarInt(in);
        for (int i = 0; i < count; i++) {
            this.details.put(helper.readString(in, 128), helper.readString(in, 4096));
        }
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writeVarInt(out, this.details.size());
        for (Map.Entry<String, String> entry : this.details.entrySet()) {
            helper.writeString(out, entry.getKey());
            helper.writeString(out, entry.getValue());
        }
    }
}
