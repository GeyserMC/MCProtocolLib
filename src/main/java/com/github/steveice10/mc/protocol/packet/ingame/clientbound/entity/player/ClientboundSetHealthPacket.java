package com.github.steveice10.mc.protocol.packet.ingame.clientbound.entity.player;

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
public class ClientboundSetHealthPacket implements Packet {
    private final float health;
    private final int food;
    private final float saturation;

    public ClientboundSetHealthPacket(NetInput in) throws IOException {
        this.health = in.readFloat();
        this.food = in.readVarInt();
        this.saturation = in.readFloat();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeFloat(this.health);
        out.writeVarInt(this.food);
        out.writeFloat(this.saturation);
    }
}
