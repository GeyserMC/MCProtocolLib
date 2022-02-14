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
public class ClientboundPlayerCombatEndPacket implements Packet {
    private final int killerId;
    private final int duration;

    public ClientboundPlayerCombatEndPacket(NetInput in) throws IOException {
        this.duration = in.readVarInt();
        this.killerId = in.readInt();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(this.duration);
        out.writeInt(this.killerId);
    }
}
