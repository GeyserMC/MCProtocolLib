package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@NoArgsConstructor
public class ClientboundStartConfigurationPacket implements MinecraftPacket {

    public ClientboundStartConfigurationPacket(ByteBuf in) {
    }

    public void serialize(ByteBuf out) {
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }

    @Override
    public boolean isTerminal() {
        return true;
    }
}
