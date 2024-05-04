package org.geysermc.mcprotocollib.protocol.packet.common.clientbound;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ClientboundTransferPacket implements MinecraftPacket {
    private final String host;
    private final int port;

    public ClientboundTransferPacket(MinecraftByteBuf buf) {
        this.host = buf.readString();
        this.port = buf.readVarInt();
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeString(this.host);
        buf.writeVarInt(this.port);
    }
}
