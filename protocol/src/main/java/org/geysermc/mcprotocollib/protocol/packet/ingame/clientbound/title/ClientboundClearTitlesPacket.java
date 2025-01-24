package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.title;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ClientboundClearTitlesPacket implements MinecraftPacket {
    private final boolean resetTimes;

    public ClientboundClearTitlesPacket(ByteBuf in) {
        this.resetTimes = in.readBoolean();
    }

    @Override
    public void serialize(ByteBuf out) {
        out.writeBoolean(this.resetTimes);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
