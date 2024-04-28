package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.level;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ClientboundSetTimePacket implements MinecraftPacket {
    private final long worldAge;
    private final long time;

    public ClientboundSetTimePacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.worldAge = in.readLong();
        this.time = in.readLong();
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        out.writeLong(this.worldAge);
        out.writeLong(this.time);
    }
}
