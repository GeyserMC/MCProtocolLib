package org.geysermc.mc.protocol.packet.common.serverbound;

import org.geysermc.mc.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mc.protocol.codec.MinecraftPacket;
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
