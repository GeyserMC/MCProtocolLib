package org.geysermc.mcprotocollib.protocol.packet.ping.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

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
