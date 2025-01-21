package org.geysermc.mcprotocollib.protocol.packet.status.serverbound;

import io.netty.buffer.ByteBuf;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@NoArgsConstructor
public class ServerboundStatusRequestPacket implements MinecraftPacket {

    public ServerboundStatusRequestPacket(ByteBuf in) {
    }

    @Override
    public void serialize(ByteBuf out) {
    }
}
