package com.github.steveice10.mc.protocol.packet.ingame.clientbound.level.border;

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
public class ClientboundInitializeBorderPacket implements Packet {
    private final double newCenterX;
    private final double newCenterZ;
    private final double oldSize;
    private final double newSize;
    private final long lerpTime;
    private final int newAbsoluteMaxSize;
    private final int warningBlocks;
    private final int warningTime;

    public ClientboundInitializeBorderPacket(NetInput in) throws IOException {
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
}
