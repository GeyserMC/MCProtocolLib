package org.geysermc.mcprotocollib.protocol.packet.login.serverbound;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@NoArgsConstructor
public class ServerboundLoginAcknowledgedPacket implements MinecraftPacket {
    public ServerboundLoginAcknowledgedPacket(MinecraftByteBuf buf) {
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
    }
}
