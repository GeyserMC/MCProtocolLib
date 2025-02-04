package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.level;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ClientboundSetTimePacket implements MinecraftPacket {
    private final long gameTime;
    private final long dayTime;
    private final boolean tickDayTime;

    public ClientboundSetTimePacket(ByteBuf in) {
        this.gameTime = in.readLong();
        this.dayTime = in.readLong();
        this.tickDayTime = in.readBoolean();
    }

    @Override
    public void serialize(ByteBuf out) {
        out.writeLong(this.gameTime);
        out.writeLong(this.dayTime);
        out.writeBoolean(this.tickDayTime);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
