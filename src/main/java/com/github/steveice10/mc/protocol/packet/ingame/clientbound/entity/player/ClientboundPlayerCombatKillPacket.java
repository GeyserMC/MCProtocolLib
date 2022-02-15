package com.github.steveice10.mc.protocol.packet.ingame.clientbound.entity.player;

import com.github.steveice10.mc.protocol.data.DefaultComponentSerializer;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import net.kyori.adventure.text.Component;

import java.io.IOException;

@Data
@With
@AllArgsConstructor
public class ClientboundPlayerCombatKillPacket implements Packet {
    private final int playerId;
    private final int killerId;
    private final Component message;

    public ClientboundPlayerCombatKillPacket(NetInput in) throws IOException {
        this.playerId = in.readVarInt();
        this.killerId = in.readInt();
        this.message = DefaultComponentSerializer.get().deserialize(in.readString());
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(this.playerId);
        out.writeInt(this.killerId);
        out.writeString(DefaultComponentSerializer.get().serialize(this.message));
    }
}
