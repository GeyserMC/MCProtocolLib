package org.geysermc.mcprotocollib.protocol.packet.configuration.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@NoArgsConstructor
public class ClientboundFinishConfigurationPacket implements MinecraftPacket {
    public ClientboundFinishConfigurationPacket(ByteBuf in, MinecraftCodecHelper helper) {
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
    }

    @Override
    public boolean isTerminal() {
        return true;
    }
}
