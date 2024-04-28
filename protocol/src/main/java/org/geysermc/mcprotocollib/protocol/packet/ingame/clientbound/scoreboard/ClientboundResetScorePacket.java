package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.scoreboard;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ClientboundResetScorePacket implements MinecraftPacket {

    private final @NonNull String owner;
    private final @Nullable String objective;

    public ClientboundResetScorePacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.owner = helper.readString(in);
        this.objective = helper.readNullable(in, helper::readString);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writeString(out, this.owner);
        helper.writeNullable(out, this.objective, helper::writeString);
    }
}
