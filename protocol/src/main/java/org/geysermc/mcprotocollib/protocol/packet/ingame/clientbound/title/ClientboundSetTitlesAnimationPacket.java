package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.title;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ClientboundSetTitlesAnimationPacket implements MinecraftPacket {
    private final int fadeIn;
    private final int stay;
    private final int fadeOut;

    public ClientboundSetTitlesAnimationPacket(ByteBuf in) {
        this.fadeIn = in.readInt();
        this.stay = in.readInt();
        this.fadeOut = in.readInt();
    }

    @Override
    public void serialize(ByteBuf out) {
        out.writeInt(this.fadeIn);
        out.writeInt(this.stay);
        out.writeInt(this.fadeOut);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
