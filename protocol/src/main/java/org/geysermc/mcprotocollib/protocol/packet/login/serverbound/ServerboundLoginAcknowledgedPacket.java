package org.geysermc.mcprotocollib.protocol.packet.login.serverbound;

import io.netty.buffer.ByteBuf;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@NoArgsConstructor
public class ServerboundLoginAcknowledgedPacket implements MinecraftPacket {
    public ServerboundLoginAcknowledgedPacket(ByteBuf in) {
    }

    @Override
    public void serialize(ByteBuf out) {
    }

    @Override
    public boolean isTerminal() {
        return true;
    }
}
