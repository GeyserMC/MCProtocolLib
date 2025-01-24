package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.level.border;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ClientboundSetBorderSizePacket implements MinecraftPacket {
    private final double size;

    public ClientboundSetBorderSizePacket(ByteBuf in) {
        this.size = in.readDouble();
    }

    @Override
    public void serialize(ByteBuf out) {
        out.writeDouble(this.size);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
