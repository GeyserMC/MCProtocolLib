package org.geysermc.mcprotocollib.protocol.packet.configuration.clientbound;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@NoArgsConstructor
public class ClientboundResetChatPacket implements MinecraftPacket {
    public ClientboundResetChatPacket(MinecraftByteBuf buf) {
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
    }
}
