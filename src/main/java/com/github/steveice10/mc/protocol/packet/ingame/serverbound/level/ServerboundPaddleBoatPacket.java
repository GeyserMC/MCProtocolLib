package com.github.steveice10.mc.protocol.packet.ingame.serverbound.level;

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
public class ServerboundPaddleBoatPacket implements Packet {
    private final boolean rightPaddleTurning;
    private final boolean leftPaddleTurning;

    public ServerboundPaddleBoatPacket(NetInput in) throws IOException {
        this.rightPaddleTurning = in.readBoolean();
        this.leftPaddleTurning = in.readBoolean();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeBoolean(this.rightPaddleTurning);
        out.writeBoolean(this.leftPaddleTurning);
    }
}
