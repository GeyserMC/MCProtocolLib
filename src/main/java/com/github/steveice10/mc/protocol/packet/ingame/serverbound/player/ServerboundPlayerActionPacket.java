package com.github.steveice10.mc.protocol.packet.ingame.serverbound.player;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import com.github.steveice10.mc.protocol.data.game.entity.object.Direction;
import com.github.steveice10.mc.protocol.data.game.entity.player.PlayerAction;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.cloudburstmc.math.vector.Vector3i;

import java.io.IOException;

@Data
@With
@AllArgsConstructor
public class ServerboundPlayerActionPacket implements MinecraftPacket {
    private final @NonNull PlayerAction action;
    private final @NonNull Vector3i position;
    private final @NonNull Direction face;
    private final int sequence;

    public ServerboundPlayerActionPacket(ByteBuf in, MinecraftCodecHelper helper) throws IOException {
        this.action = PlayerAction.from(helper.readVarInt(in));
        this.position = helper.readPosition(in);
        this.face = Direction.VALUES[in.readUnsignedByte()];
        this.sequence = helper.readVarInt(in);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
        helper.writeVarInt(out, this.action.ordinal());
        helper.writePosition(out, this.position);
        out.writeByte(this.face.ordinal());
        helper.writeVarInt(out, this.sequence);
    }
}
