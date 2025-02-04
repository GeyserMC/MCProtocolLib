package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.scoreboard;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;

@Data
@With
@AllArgsConstructor
public class ClientboundResetScorePacket implements MinecraftPacket {

    private final @NonNull String owner;
    private final @Nullable String objective;

    public ClientboundResetScorePacket(ByteBuf in) {
        this.owner = MinecraftTypes.readString(in);
        this.objective = MinecraftTypes.readNullable(in, MinecraftTypes::readString);
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeString(out, this.owner);
        MinecraftTypes.writeNullable(out, this.objective, MinecraftTypes::writeString);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
