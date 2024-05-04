package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@NoArgsConstructor
public class ClientboundStartConfigurationPacket implements MinecraftPacket {

    public ClientboundStartConfigurationPacket(MinecraftByteBuf buf) {
    }

    public void serialize(MinecraftByteBuf buf) {
    }
}
