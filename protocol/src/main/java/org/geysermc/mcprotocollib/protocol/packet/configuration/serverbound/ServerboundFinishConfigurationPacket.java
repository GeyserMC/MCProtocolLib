package org.geysermc.mcprotocollib.protocol.packet.configuration.serverbound;

import io.netty.buffer.ByteBuf;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@NoArgsConstructor
public class ServerboundFinishConfigurationPacket implements MinecraftPacket {

    public ServerboundFinishConfigurationPacket(ByteBuf in) {
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
