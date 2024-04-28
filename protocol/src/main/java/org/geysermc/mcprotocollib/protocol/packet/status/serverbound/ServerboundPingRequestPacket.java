package org.geysermc.mcprotocollib.protocol.packet.status.serverbound;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ServerboundPingRequestPacket implements MinecraftPacket {
    private final long pingTime;

    public ServerboundPingRequestPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.pingTime = in.readLong();
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        out.writeLong(this.pingTime);
    }
}
