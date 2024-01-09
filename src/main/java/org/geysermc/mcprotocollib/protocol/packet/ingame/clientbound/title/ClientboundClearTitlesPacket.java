package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.title;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;

@Data
@With
@AllArgsConstructor
public class ClientboundClearTitlesPacket implements MinecraftPacket {
    private final boolean resetTimes;

    public ClientboundClearTitlesPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.resetTimes = in.readBoolean();
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        out.writeBoolean(this.resetTimes);
    }
}
