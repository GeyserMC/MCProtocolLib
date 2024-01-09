package org.geysermc.mcprotocollib.protocol.packet.common.serverbound;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;

@Data
@With
@AllArgsConstructor
public class ServerboundKeepAlivePacket implements MinecraftPacket {
    private final long pingId;

    public ServerboundKeepAlivePacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.pingId = in.readLong();
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        out.writeLong(this.pingId);
    }

}
