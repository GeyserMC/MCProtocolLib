package com.github.steveice10.mc.protocol.packet.ingame.server.entity.player;

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
public class ServerPlayerCombatEndPacket implements Packet {
    private int killerId;
    private int duration;

    @Override
    public void read(NetInput in) throws IOException {
        this.duration = in.readVarInt();
        this.killerId = in.readInt();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(this.duration);
        out.writeInt(this.killerId);
    }

    @Override
    public boolean isPriority() {
        return false;
    }
}
