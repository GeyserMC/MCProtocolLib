package org.geysermc.mcprotocollib.protocol.packet.ping.serverbound;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ServerboundPingRequestPacket implements MinecraftPacket {
    private final long pingTime;

    public ServerboundPingRequestPacket(ByteBuf in) {
        this.pingTime = in.readLong();
    }

    @Override
    public void serialize(ByteBuf out) {
        out.writeLong(this.pingTime);
    }
}
