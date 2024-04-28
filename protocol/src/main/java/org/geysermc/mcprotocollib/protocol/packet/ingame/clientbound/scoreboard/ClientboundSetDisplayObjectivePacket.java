package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.scoreboard;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.data.game.scoreboard.ScoreboardPosition;

@Data
@With
@AllArgsConstructor
public class ClientboundSetDisplayObjectivePacket implements MinecraftPacket {
    private final @NonNull ScoreboardPosition position;
    private final @NonNull String name;

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
