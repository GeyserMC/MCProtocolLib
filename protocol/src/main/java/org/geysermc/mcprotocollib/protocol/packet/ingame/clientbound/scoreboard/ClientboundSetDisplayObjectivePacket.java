package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.scoreboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.data.game.scoreboard.ScoreboardPosition;

@Data
@With
@AllArgsConstructor
public class ClientboundSetDisplayObjectivePacket implements MinecraftPacket {
    private final @NonNull ScoreboardPosition position;
    private final @NonNull String name;

    public ClientboundSetDisplayObjectivePacket(MinecraftByteBuf buf) {
        this.position = ScoreboardPosition.from(buf.readVarInt());
        this.name = buf.readString();
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeVarInt(this.position.ordinal());
        buf.writeString(this.name);
    }
}
