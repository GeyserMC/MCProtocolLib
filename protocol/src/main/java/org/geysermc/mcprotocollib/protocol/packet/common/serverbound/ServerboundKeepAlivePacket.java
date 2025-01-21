package org.geysermc.mcprotocollib.protocol.packet.common.serverbound;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ServerboundKeepAlivePacket implements MinecraftPacket {
    private final long pingId;

    public ServerboundKeepAlivePacket(ByteBuf in) {
        this.pingId = in.readLong();
    }

    @Override
    public void serialize(ByteBuf out) {
        out.writeLong(this.pingId);
    }

}
