package com.github.steveice10.mc.protocol.packet.ingame.clientbound.entity.player;

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
import com.github.steveice10.mc.protocol.data.game.entity.player.PlayerAction;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.With;

import java.io.IOException;

@Data
@With
@Setter(AccessLevel.NONE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ClientboundBlockBreakAckPacket implements Packet {
    private @NonNull PlayerAction action;
    private boolean successful;
    private @NonNull Position position;
    private int newState;

    @Override
    public void read(NetInput in) throws IOException {
        this.position = Position.read(in);
        this.newState = in.readVarInt();
        this.action = MagicValues.key(PlayerAction.class, in.readVarInt());
        this.successful = in.readBoolean();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        Position.write(out, this.position);
        out.writeVarInt(this.newState);
        out.writeVarInt(MagicValues.value(Integer.class, this.action));
        out.writeBoolean(this.successful);
    }

    @Override
    public boolean isPriority() {
        return false;
    }
}
