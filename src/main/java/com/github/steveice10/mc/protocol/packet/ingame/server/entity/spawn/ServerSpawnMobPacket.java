package com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn;

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.entity.type.MobType;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import java.io.IOException;
import java.util.UUID;

@Data
@Setter(AccessLevel.NONE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ServerSpawnMobPacket implements Packet {
    private int entityId;
    private @NonNull UUID uuid;
    private @NonNull MobType type;
    private double x;
    private double y;
    private double z;
    private float pitch;
    private float yaw;
    private float headYaw;
    private double motionX;
    private double motionY;
    private double motionZ;

    @Override
    public void read(NetInput in) throws IOException {
        this.entityId = in.readVarInt();
        this.uuid = in.readUUID();
        this.type = MagicValues.key(MobType.class, in.readVarInt());
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
        out.writeVarInt(MagicValues.value(Integer.class, this.type));
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

    @Override
    public boolean isPriority() {
        return false;
    }
}
