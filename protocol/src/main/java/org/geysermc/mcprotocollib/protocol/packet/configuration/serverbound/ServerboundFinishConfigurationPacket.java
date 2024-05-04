package org.geysermc.mcprotocollib.protocol.packet.configuration.serverbound;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@NoArgsConstructor
public class ServerboundFinishConfigurationPacket implements MinecraftPacket {

    public ServerboundFinishConfigurationPacket(MinecraftByteBuf buf) {
    }

    public void serialize(MinecraftByteBuf buf) {
    }
}
