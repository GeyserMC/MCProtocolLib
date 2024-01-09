package org.geysermc.mcprotocollib.protocol.packet.status.clientbound;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;

@Data
@With
@AllArgsConstructor
public class ClientboundPongResponsePacket implements MinecraftPacket {
    private final long pingTime;

    public ClientboundPongResponsePacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.pingTime = in.readLong();
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        out.writeLong(this.pingTime);
    }
}
