package org.geysermc.mcprotocollib.protocol.packet.status.serverbound;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@NoArgsConstructor
public class ServerboundStatusRequestPacket implements MinecraftPacket {

    public ServerboundStatusRequestPacket(MinecraftByteBuf buf) {
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
    }
}
