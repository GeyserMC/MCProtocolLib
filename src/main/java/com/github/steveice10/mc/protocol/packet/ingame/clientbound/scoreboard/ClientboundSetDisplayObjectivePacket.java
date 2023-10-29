package com.github.steveice10.mc.protocol.packet.ingame.clientbound.scoreboard;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import com.github.steveice10.mc.protocol.data.game.scoreboard.ScoreboardPosition;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.jetbrains.annotations.NotNull;

@Data
@With
@AllArgsConstructor
public class ClientboundSetDisplayObjectivePacket implements MinecraftPacket {
    private final @NotNull ScoreboardPosition position;
    private final @NotNull String name;

    public ClientboundSetDisplayObjectivePacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.position = ScoreboardPosition.from(helper.readVarInt(in));
        this.name = helper.readString(in);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writeVarInt(out, this.position.ordinal());
        helper.writeString(out, this.name);
    }
}
