package org.geysermc.mcprotocollib.protocol.packet.common.serverbound;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ServerboundPongPacket implements MinecraftPacket {
    private final int id;

    public ServerboundPongPacket(MinecraftByteBuf buf) {
        this.id = buf.readInt();
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeInt(this.id);
    }
}
