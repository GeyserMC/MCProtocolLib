package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.entity.spawn;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ClientboundAddExperienceOrbPacket implements MinecraftPacket {
    private final int entityId;
    private final double x;
    private final double y;
    private final double z;
    private final int exp;

    public ClientboundAddExperienceOrbPacket(MinecraftByteBuf buf) {
        this.entityId = buf.readVarInt();
        this.x = buf.readDouble();
        this.y = buf.readDouble();
        this.z = buf.readDouble();
        this.exp = buf.readShort();
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeVarInt(this.entityId);
        buf.writeDouble(this.x);
        buf.writeDouble(this.y);
        buf.writeDouble(this.z);
        buf.writeShort(this.exp);
    }
}
