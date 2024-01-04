package org.geysermc.mc.protocol.packet.common.clientbound;

import org.geysermc.mc.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mc.protocol.codec.MinecraftPacket;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;

@Data
@With
@AllArgsConstructor
public class ClientboundPingPacket implements MinecraftPacket {
    private final int id;

    public ClientboundPingPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.id = in.readInt();
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        out.writeInt(this.id);
    }
}
