package com.github.steveice10.mc.protocol.packet.ingame.clientbound.scoreboard;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import com.github.steveice10.mc.protocol.data.game.scoreboard.ScoreboardAction;
import io.netty.buffer.ByteBuf;
import lombok.*;

import java.io.IOException;

@Data
@With
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientboundSetScorePacket implements MinecraftPacket {
    private final @NonNull String entry;
    private final @NonNull ScoreboardAction action;
    private final @NonNull String objective;
    private final int value;

    /**
     * Creates a set score packet that removes the entry.
     */
    public ClientboundSetScorePacket(@NonNull String entry, @NonNull String objective) {
        this.entry = entry;
        this.action = ScoreboardAction.REMOVE;
        this.objective = objective;
        this.value = 0;
    }

    /**
     * Creates a set score packet that adds or updates the entry.
     */
    public ClientboundSetScorePacket(@NonNull String entry, @NonNull String objective, int value) {
        this.entry = entry;
        this.action = ScoreboardAction.ADD_OR_UPDATE;
        this.objective = objective;

        this.value = value;
    }

    public ClientboundSetScorePacket(ByteBuf in, MinecraftCodecHelper helper) throws IOException {
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
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
        helper.writeString(out, this.entry);
        helper.writeVarInt(out, this.action.ordinal());
        helper.writeString(out, this.objective);
        if (this.action == ScoreboardAction.ADD_OR_UPDATE) {
            helper.writeVarInt(out, this.value);
        }
    }
}
