package org.geysermc.mcprotocollib.protocol.packet.configuration.serverbound;

import io.netty.buffer.ByteBuf;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@NoArgsConstructor
public class ServerboundFinishConfigurationPacket implements MinecraftPacket {

    public ServerboundFinishConfigurationPacket(ByteBuf in, MinecraftCodecHelper helper) {
    }

    public void serialize(ByteBuf buf, MinecraftCodecHelper helper) {
    }

    @Override
    public boolean isTerminal() {
        return true;
    }
}
