package com.github.steveice10.mc.protocol.packet.ingame.serverbound.player;

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
public class ServerboundMovePlayerRotPacket implements Packet {
    private final boolean onGround;
    private final float yaw;
    private final float pitch;

    public ServerboundMovePlayerRotPacket(NetInput in) throws IOException {
        this.yaw = in.readFloat();
        this.pitch = in.readFloat();
        this.onGround = in.readBoolean();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeFloat(this.yaw);
        out.writeFloat(this.pitch);
        out.writeBoolean(this.onGround);
    }
}
