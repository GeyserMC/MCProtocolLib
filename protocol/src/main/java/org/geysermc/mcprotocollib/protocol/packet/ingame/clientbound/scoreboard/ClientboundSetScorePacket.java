package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.scoreboard;

import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;
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

    public ClientboundSetScorePacket(ByteBuf in) {
        this.owner = MinecraftTypes.readString(in);
        this.objective = MinecraftTypes.readString(in);
        this.value = MinecraftTypes.readVarInt(in);
        this.display = MinecraftTypes.readNullable(in, MinecraftTypes::readComponent);
        this.numberFormat = MinecraftTypes.readNullable(in, MinecraftTypes::readNumberFormat);
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeString(out, this.owner);
        MinecraftTypes.writeString(out, this.objective);
        MinecraftTypes.writeVarInt(out, this.value);
        MinecraftTypes.writeNullable(out, this.display, MinecraftTypes::writeComponent);
        MinecraftTypes.writeNullable(out, this.numberFormat, MinecraftTypes::writeNumberFormat);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
