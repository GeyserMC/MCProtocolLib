package com.github.steveice10.mc.protocol.packet.ingame.serverbound.player;

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
import com.github.steveice10.mc.protocol.data.game.entity.object.Direction;
import com.github.steveice10.mc.protocol.data.game.entity.player.PlayerAction;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;

import java.io.IOException;

@Data
@With
@AllArgsConstructor
public class ServerboundPlayerActionPacket implements Packet {
    private final @NonNull PlayerAction action;
    private final @NonNull Position position;
    private final @NonNull Direction face;

    public ServerboundPlayerActionPacket(NetInput in) throws IOException {
        this.action = MagicValues.key(PlayerAction.class, in.readVarInt());
        this.position = Position.read(in);
        this.face = Direction.VALUES[in.readUnsignedByte()];
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(MagicValues.value(Integer.class, this.action));
        Position.write(out, this.position);
        out.writeByte(this.face.ordinal());
    }
}
