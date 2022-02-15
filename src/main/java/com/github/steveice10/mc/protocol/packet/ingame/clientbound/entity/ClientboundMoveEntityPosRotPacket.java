package com.github.steveice10.mc.protocol.packet.ingame.clientbound.entity;

import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;

import java.io.IOException;

@Data
@With
@AllArgsConstructor
public class ClientboundMoveEntityPosRotPacket implements Packet {
    private final int entityId;
    private final double moveX;
    private final double moveY;
    private final double moveZ;
    private final float yaw;
    private final float pitch;
    private final boolean onGround;

    public ClientboundMoveEntityPosRotPacket(NetInput in) throws IOException {
        this.entityId = in.readVarInt();
        this.moveX = in.readShort() / 4096D;
        this.moveY = in.readShort() / 4096D;
        this.moveZ = in.readShort() / 4096D;
        this.yaw = in.readByte() * 360 / 256f;
        this.pitch = in.readByte() * 360 / 256f;
        this.onGround = in.readBoolean();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(this.entityId);
        out.writeShort((int) (this.moveX * 4096));
        out.writeShort((int) (this.moveY * 4096));
        out.writeShort((int) (this.moveZ * 4096));
        out.writeByte((byte) (this.yaw * 256 / 360));
        out.writeByte((byte) (this.pitch * 256 / 360));
        out.writeBoolean(this.onGround);
    }
}
