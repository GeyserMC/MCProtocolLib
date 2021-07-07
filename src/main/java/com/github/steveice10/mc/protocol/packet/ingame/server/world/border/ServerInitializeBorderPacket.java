package com.github.steveice10.mc.protocol.packet.ingame.server.world.border;

import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.*;

import java.io.IOException;

@Data
@With
@Setter(AccessLevel.NONE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ServerInitializeBorderPacket implements Packet {
    private double newCenterX;
    private double newCenterZ;
    private double oldSize;
    private double newSize;
    private long lerpTime;
    private int newAbsoluteMaxSize;
    private int warningBlocks;
    private int warningTime;

    @Override
    public void read(NetInput in) throws IOException {
        this.newCenterX = in.readDouble();
        this.newCenterZ = in.readDouble();
        this.oldSize = in.readDouble();
        this.newSize = in.readDouble();
        this.lerpTime = in.readVarLong();
        this.newAbsoluteMaxSize = in.readVarInt();
        this.warningBlocks = in.readVarInt();
        this.warningTime = in.readVarInt();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeDouble(this.newCenterX);
        out.writeDouble(this.newCenterZ);
        out.writeDouble(this.oldSize);
        out.writeDouble(this.newSize);
        out.writeVarLong(this.lerpTime);
        out.writeVarInt(this.newAbsoluteMaxSize);
        out.writeVarInt(this.warningBlocks);
        out.writeVarInt(this.warningTime);
    }

    @Override
    public boolean isPriority() {
        return false;
    }
}
