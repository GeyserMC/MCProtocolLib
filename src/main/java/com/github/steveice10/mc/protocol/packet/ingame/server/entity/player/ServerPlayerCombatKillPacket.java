package com.github.steveice10.mc.protocol.packet.ingame.server.entity.player;

import com.github.steveice10.mc.protocol.data.DefaultComponentSerializer;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.*;
import net.kyori.adventure.text.Component;

import java.io.IOException;

@Data
@With
@Setter(AccessLevel.NONE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ServerPlayerCombatKillPacket implements Packet {
    private int playerId;
    private int killerId;
    private Component message;

    @Override
    public void read(NetInput in) throws IOException {
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

    @Override
    public boolean isPriority() {
        return false;
    }
}
