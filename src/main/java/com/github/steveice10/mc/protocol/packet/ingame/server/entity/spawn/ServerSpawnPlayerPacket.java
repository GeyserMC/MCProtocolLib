package com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn;

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
public class ServerSpawnPlayerPacket implements Packet {
    private int entityId;
    private @NonNull UUID uuid;
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;

    @Override
    public void read(NetInput in) throws IOException {
        this.entityId = in.readVarInt();
        this.uuid = in.readUUID();
        this.x = in.readDouble();
        this.y = in.readDouble();
        this.z = in.readDouble();
        this.yaw = in.readByte() * 360 / 256f;
        this.pitch = in.readByte() * 360 / 256f;
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(this.entityId);
        out.writeUUID(this.uuid);
        out.writeDouble(this.x);
        out.writeDouble(this.y);
        out.writeDouble(this.z);
        out.writeByte((byte) (this.yaw * 256 / 360));
        out.writeByte((byte) (this.pitch * 256 / 360));
    }

    @Override
    public boolean isPriority() {
        return false;
    }
}
