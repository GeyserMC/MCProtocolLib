package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.scoreboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ClientboundResetScorePacket implements MinecraftPacket {

    private final @NonNull String owner;
    private final @Nullable String objective;

    public ClientboundResetScorePacket(MinecraftByteBuf buf) {
        this.owner = buf.readString();
        this.objective = buf.readNullable(buf::readString);
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeString(this.owner);
        buf.writeNullable(this.objective, buf::writeString);
    }
}
