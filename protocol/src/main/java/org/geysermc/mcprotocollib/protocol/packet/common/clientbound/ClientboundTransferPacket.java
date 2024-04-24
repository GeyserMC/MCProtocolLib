package org.geysermc.mcprotocollib.protocol.packet.common.clientbound;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;

@Data
@With
@AllArgsConstructor
public class ClientboundTransferPacket implements MinecraftPacket {
    private final String host;
    private final int port;

    public ClientboundTransferPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.host = helper.readString(in);
        this.port = helper.readVarInt(in);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writeString(out, this.host);
        helper.writeVarInt(out, this.port);
    }
}
