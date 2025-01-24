package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.scoreboard;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;
import org.geysermc.mcprotocollib.protocol.data.game.scoreboard.ScoreboardPosition;

@Data
@With
@AllArgsConstructor
public class ClientboundSetDisplayObjectivePacket implements MinecraftPacket {
    private final @NonNull ScoreboardPosition position;
    private final @NonNull String name;

    public ClientboundSetDisplayObjectivePacket(ByteBuf in) {
        this.position = ScoreboardPosition.from(MinecraftTypes.readVarInt(in));
        this.name = MinecraftTypes.readString(in);
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeVarInt(out, this.position.ordinal());
        MinecraftTypes.writeString(out, this.name);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
