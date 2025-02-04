package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.entity.spawn;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;

@Data
@With
@AllArgsConstructor
public class ClientboundAddExperienceOrbPacket implements MinecraftPacket {
    private final int entityId;
    private final double x;
    private final double y;
    private final double z;
    private final int exp;

    public ClientboundAddExperienceOrbPacket(ByteBuf in) {
        this.entityId = MinecraftTypes.readVarInt(in);
        this.x = in.readDouble();
        this.y = in.readDouble();
        this.z = in.readDouble();
        this.exp = in.readShort();
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeVarInt(out, this.entityId);
        out.writeDouble(this.x);
        out.writeDouble(this.y);
        out.writeDouble(this.z);
        out.writeShort(this.exp);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
