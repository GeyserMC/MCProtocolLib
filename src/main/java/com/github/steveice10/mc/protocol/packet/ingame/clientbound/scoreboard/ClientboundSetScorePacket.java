package com.github.steveice10.mc.protocol.packet.ingame.clientbound.scoreboard;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import com.github.steveice10.mc.protocol.data.game.scoreboard.ScoreboardAction;
import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.jetbrains.annotations.NotNull;

@Data
@With
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientboundSetScorePacket implements MinecraftPacket {
    private final @NotNull String entry;
    private final @NotNull ScoreboardAction action;
    private final @NotNull String objective;
    private final int value;

    /**
     * Creates a set score packet that removes the entry.
     *
     * @param entry     The entry to remove.
     * @param objective The objective to remove the entry from.
     */
    public ClientboundSetScorePacket(@NotNull String entry, @NotNull String objective) {
        this.entry = entry;
        this.action = ScoreboardAction.REMOVE;
        this.objective = objective;
        this.value = 0;
    }

    /**
     * Creates a set score packet that adds or updates the entry.
     *
     * @param entry     The entry to add or update.
     * @param objective The objective to add or update the entry in.
     * @param value     The value to set the entry to.
     */
    public ClientboundSetScorePacket(@NotNull String entry, @NotNull String objective, int value) {
        this.entry = entry;
        this.action = ScoreboardAction.ADD_OR_UPDATE;
        this.objective = objective;

        this.value = value;
    }

    public ClientboundSetScorePacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.entry = helper.readString(in);
        this.action = ScoreboardAction.from(helper.readVarInt(in));
        this.objective = helper.readString(in);
        if (this.action == ScoreboardAction.ADD_OR_UPDATE) {
            this.value = helper.readVarInt(in);
        } else {
            this.value = 0;
        }
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writeString(out, this.entry);
        helper.writeVarInt(out, this.action.ordinal());
        helper.writeString(out, this.objective);
        if (this.action == ScoreboardAction.ADD_OR_UPDATE) {
            helper.writeVarInt(out, this.value);
        }
    }
}
