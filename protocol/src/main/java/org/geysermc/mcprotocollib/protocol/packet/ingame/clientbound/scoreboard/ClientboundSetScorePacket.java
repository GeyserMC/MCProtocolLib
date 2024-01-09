package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.scoreboard;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.data.game.chat.numbers.NumberFormat;
import io.netty.buffer.ByteBuf;
import lombok.*;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.IOException;

@Data
@With
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientboundSetScorePacket implements MinecraftPacket {
    private final @NonNull String owner;
    private final @NonNull String objective;
    private final int value;
    private final @Nullable Component display;
    private final @Nullable NumberFormat numberFormat;

    /**
     * Creates a set score packet that adds or updates the entry.
     */
    public ClientboundSetScorePacket(@NonNull String owner, @NonNull String objective, int value) {
        this.owner = owner;
        this.objective = objective;
        this.value = value;
        this.display = null;
        this.numberFormat = null;
    }

    public ClientboundSetScorePacket(ByteBuf in, MinecraftCodecHelper helper) throws IOException {
        this.owner = helper.readString(in);
        this.objective = helper.readString(in);
        this.value = helper.readVarInt(in);
        this.display = helper.readNullable(in, helper::readComponent);
        this.numberFormat = helper.readNullable(in, helper::readNumberFormat);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
        helper.writeString(out, this.owner);
        helper.writeString(out, this.objective);
        helper.writeVarInt(out, this.value);
        helper.writeNullable(out, this.display, helper::writeComponent);
        helper.writeNullable(out, this.numberFormat, helper::writeNumberFormat);
    }
}
