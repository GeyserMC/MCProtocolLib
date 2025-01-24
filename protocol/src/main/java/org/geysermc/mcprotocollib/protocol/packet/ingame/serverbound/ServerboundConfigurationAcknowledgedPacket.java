package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound;

import io.netty.buffer.ByteBuf;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@NoArgsConstructor
public class ServerboundConfigurationAcknowledgedPacket implements MinecraftPacket {

    public ServerboundConfigurationAcknowledgedPacket(ByteBuf in) {
    }

    public void serialize(ByteBuf out) {
    }

    @Override
    public boolean isTerminal() {
        return true;
    }
}
