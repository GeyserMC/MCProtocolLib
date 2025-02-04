package org.geysermc.mcprotocollib.protocol.packet.common.serverbound;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ServerboundPongPacket implements MinecraftPacket {
    private final int id;

    public ServerboundPongPacket(ByteBuf in) {
        this.id = in.readInt();
    }

    @Override
    public void serialize(ByteBuf out) {
        out.writeInt(this.id);
    }
}
