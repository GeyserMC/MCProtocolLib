package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.level;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;

@Data
@With
@AllArgsConstructor
public class ServerboundAcceptTeleportationPacket implements MinecraftPacket {
    private final int id;
    private final double x;
    private final double y;
    private final double z;
    private final float yRot;
    private final float xRot;

    public ServerboundAcceptTeleportationPacket(ByteBuf in) {
        this.id = MinecraftTypes.readVarInt(in);
        this.x = in.readDouble();
        this.y = in.readDouble();
        this.z = in.readDouble();
        this.yRot = in.readFloat();
        this.xRot = in.readFloat();
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeVarInt(out, this.id);
        out.writeDouble(this.x);
        out.writeDouble(this.y);
        out.writeDouble(this.z);
        out.writeFloat(this.yRot);
        out.writeFloat(this.xRot);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
