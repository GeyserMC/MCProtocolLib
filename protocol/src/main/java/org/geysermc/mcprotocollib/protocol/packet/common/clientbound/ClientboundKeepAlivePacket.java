package org.geysermc.mcprotocollib.protocol.packet.common.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ClientboundKeepAlivePacket implements MinecraftPacket {
    private final long pingId;

    public ClientboundKeepAlivePacket(ByteBuf in) {
        this.pingId = in.readLong();
    }

    @Override
    public void serialize(ByteBuf out) {
        out.writeLong(this.pingId);
    }
}
