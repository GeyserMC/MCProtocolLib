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
public class ServerboundMovePlayerStatusOnlyPacket implements Packet {
    private final boolean onGround;

    public ServerboundMovePlayerStatusOnlyPacket(NetInput in) throws IOException {
        this.onGround = in.readBoolean();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeBoolean(this.onGround);
    }
}
