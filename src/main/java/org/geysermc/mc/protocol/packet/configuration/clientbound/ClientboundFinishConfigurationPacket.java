package org.geysermc.mc.protocol.packet.configuration.clientbound;

import org.geysermc.mc.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mc.protocol.codec.MinecraftPacket;
import io.netty.buffer.ByteBuf;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ClientboundFinishConfigurationPacket implements MinecraftPacket {
    public ClientboundFinishConfigurationPacket(ByteBuf in, MinecraftCodecHelper helper) {
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
    }
}
