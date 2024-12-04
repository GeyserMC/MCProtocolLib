package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.entity;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.cloudburstmc.math.vector.Vector3d;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ClientboundEntityPositionSyncPacket implements MinecraftPacket {
    private final int id;
    private final Vector3d position;
    private final Vector3d deltaMovement;
    private final float yRot;
    private final float xRot;
    private final boolean onGround;

    public ClientboundEntityPositionSyncPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.id = helper.readVarInt(in);
        this.position = Vector3d.from(in.readDouble(), in.readDouble(), in.readDouble());
        this.deltaMovement = Vector3d.from(in.readDouble(), in.readDouble(), in.readDouble());
        this.yRot = in.readFloat();
        this.xRot = in.readFloat();
        this.onGround = in.readBoolean();
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writeVarInt(out, this.id);
        out.writeDouble(this.position.getX());
        out.writeDouble(this.position.getY());
        out.writeDouble(this.position.getZ());
        out.writeDouble(this.deltaMovement.getX());
        out.writeDouble(this.deltaMovement.getY());
        out.writeDouble(this.deltaMovement.getZ());
        out.writeFloat(this.yRot);
        out.writeFloat(this.xRot);
        out.writeBoolean(this.onGround);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
