package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.title;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ClientboundSetTitlesAnimationPacket implements MinecraftPacket {
    private final int fadeIn;
    private final int stay;
    private final int fadeOut;

    public ClientboundSetTitlesAnimationPacket(MinecraftByteBuf buf) {
        this.fadeIn = buf.readInt();
        this.stay = buf.readInt();
        this.fadeOut = buf.readInt();
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeInt(this.fadeIn);
        buf.writeInt(this.stay);
        buf.writeInt(this.fadeOut);
    }
}
