package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.scoreboard;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.data.game.chat.numbers.NumberFormat;

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

    public ClientboundSetScorePacket(MinecraftByteBuf buf) {
        this.owner = buf.readString();
        this.objective = buf.readString();
        this.value = buf.readVarInt();
        this.display = buf.readNullable(buf::readComponent);
        this.numberFormat = buf.readNullable(buf::readNumberFormat);
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeString(this.owner);
        buf.writeString(this.objective);
        buf.writeVarInt(this.value);
        buf.writeNullable(this.display, buf::writeComponent);
        buf.writeNullable(this.numberFormat, buf::writeNumberFormat);
    }
}
