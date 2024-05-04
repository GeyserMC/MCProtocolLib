package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.level;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ClientboundSetTimePacket implements MinecraftPacket {
    private final long worldAge;
    private final long time;

    public ClientboundSetTimePacket(MinecraftByteBuf buf) {
        this.worldAge = buf.readLong();
        this.time = buf.readLong();
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeLong(this.worldAge);
        buf.writeLong(this.time);
    }
}
