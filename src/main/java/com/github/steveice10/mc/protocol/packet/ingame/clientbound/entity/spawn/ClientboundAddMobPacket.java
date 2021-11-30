package com.github.steveice10.mc.protocol.packet.ingame.clientbound.entity.spawn;

import com.github.steveice10.mc.protocol.data.game.entity.type.EntityType;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;

import java.io.IOException;
import java.util.UUID;

@Data
@With
@AllArgsConstructor
public class ClientboundAddMobPacket implements Packet {
    private final int entityId;
    private final @NonNull UUID uuid;
    private final @NonNull EntityType type;
    private final double x;
    private final double y;
    private final double z;
    private final float pitch;
    private final float yaw;
    private final float headYaw;
    private final double motionX;
    private final double motionY;
    private final double motionZ;

    public ClientboundAddMobPacket(NetInput in) throws IOException {
        this.entityId = in.readVarInt();
        this.uuid = in.readUUID();
        this.type = EntityType.read(in);
        this.x = in.readDouble();
        this.y = in.readDouble();
        this.z = in.readDouble();
        this.yaw = in.readByte() * 360 / 256f;
        this.pitch = in.readByte() * 360 / 256f;
        this.headYaw = in.readByte() * 360 / 256f;
        this.motionX = in.readShort() / 8000D;
        this.motionY = in.readShort() / 8000D;
        this.motionZ = in.readShort() / 8000D;
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(this.entityId);
        out.writeUUID(this.uuid);
        out.writeVarInt(this.type.ordinal());
        out.writeDouble(this.x);
        out.writeDouble(this.y);
        out.writeDouble(this.z);
        out.writeByte((byte) (this.yaw * 256 / 360));
        out.writeByte((byte) (this.pitch * 256 / 360));
        out.writeByte((byte) (this.headYaw * 256 / 360));
        out.writeShort((int) (this.motionX * 8000));
        out.writeShort((int) (this.motionY * 8000));
        out.writeShort((int) (this.motionZ * 8000));
    }
}
