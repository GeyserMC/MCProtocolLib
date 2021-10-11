package com.github.steveice10.mc.protocol.packet.ingame.server.entity.player;

import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.With;

import java.io.IOException;

@Data
@With
@Setter(AccessLevel.NONE)
public class ServerPlayerCombatEnterPacket implements Packet {
    @Override
    public void read(NetInput in) throws IOException {
        // no-op
    }

    @Override
    public void write(NetOutput out) throws IOException {
        // no-op
    }

    @Override
    public boolean isPriority() {
        return false;
    }
}
