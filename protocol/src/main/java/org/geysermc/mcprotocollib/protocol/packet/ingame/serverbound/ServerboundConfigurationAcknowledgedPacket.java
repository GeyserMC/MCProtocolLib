package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@NoArgsConstructor
public class ServerboundConfigurationAcknowledgedPacket implements MinecraftPacket {

    public ServerboundConfigurationAcknowledgedPacket(MinecraftByteBuf buf) {
    }

    public void serialize(MinecraftByteBuf buf) {
    }
}
