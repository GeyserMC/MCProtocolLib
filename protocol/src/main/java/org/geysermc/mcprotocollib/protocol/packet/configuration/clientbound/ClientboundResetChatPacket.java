package org.geysermc.mcprotocollib.protocol.packet.configuration.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@NoArgsConstructor
public class ClientboundResetChatPacket implements MinecraftPacket {
    public ClientboundResetChatPacket(ByteBuf in) {
    }

    @Override
    public void serialize(ByteBuf out) {
    }
}
