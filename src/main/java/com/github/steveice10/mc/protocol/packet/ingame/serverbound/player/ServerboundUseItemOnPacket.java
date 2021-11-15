package com.github.steveice10.mc.protocol.packet.ingame.serverbound.player;

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
import com.github.steveice10.mc.protocol.data.game.entity.object.Direction;
import com.github.steveice10.mc.protocol.data.game.entity.player.Hand;
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
public class ServerboundUseItemOnPacket implements Packet {
    private final @NonNull Position position;
    private final @NonNull Direction face;
    private final @NonNull Hand hand;
    private final float cursorX;
    private final float cursorY;
    private final float cursorZ;
    private final boolean insideBlock;

    public ServerboundUseItemOnPacket(NetInput in) throws IOException {
        this.hand = MagicValues.key(Hand.class, in.readVarInt());
        this.position = Position.read(in);
        this.face = in.readEnum(Direction.VALUES);
        this.cursorX = in.readFloat();
        this.cursorY = in.readFloat();
        this.cursorZ = in.readFloat();
        this.insideBlock = in.readBoolean();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(MagicValues.value(Integer.class, this.hand));
        Position.write(out, this.position);
        out.writeEnum(this.face);
        out.writeFloat(this.cursorX);
        out.writeFloat(this.cursorY);
        out.writeFloat(this.cursorZ);
        out.writeBoolean(this.insideBlock);
    }
}
