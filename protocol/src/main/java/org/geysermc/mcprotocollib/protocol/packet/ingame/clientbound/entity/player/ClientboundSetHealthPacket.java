package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.entity.player;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ClientboundSetHealthPacket implements MinecraftPacket {
    private final float health;
    private final int food;
    private final float saturation;

    public ClientboundSetHealthPacket(MinecraftByteBuf buf) {
        this.health = buf.readFloat();
        this.food = buf.readVarInt();
        this.saturation = buf.readFloat();
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeFloat(this.health);
        buf.writeVarInt(this.food);
        buf.writeFloat(this.saturation);
    }
}
