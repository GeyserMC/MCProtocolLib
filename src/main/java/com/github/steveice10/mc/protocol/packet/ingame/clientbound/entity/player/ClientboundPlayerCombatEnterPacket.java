package com.github.steveice10.mc.protocol.packet.ingame.clientbound.entity.player;

import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.*;

import java.io.IOException;

@Data
@With
@NoArgsConstructor
public class ClientboundPlayerCombatEnterPacket implements Packet {

    public ClientboundPlayerCombatEnterPacket(NetInput in) {
        // no-op
    }

    @Override
    public void write(NetOutput out) throws IOException {
        // no-op
    }
}
