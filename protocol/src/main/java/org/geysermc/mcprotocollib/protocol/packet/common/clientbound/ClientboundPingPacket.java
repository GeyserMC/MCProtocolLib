package org.geysermc.mcprotocollib.protocol.packet.common.clientbound;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ClientboundPingPacket implements MinecraftPacket {
    private final int id;

    public ClientboundPingPacket(MinecraftByteBuf buf) {
        this.id = buf.readInt();
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeInt(this.id);
    }
}
