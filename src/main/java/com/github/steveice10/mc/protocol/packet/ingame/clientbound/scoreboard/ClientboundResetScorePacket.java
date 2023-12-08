package com.github.steveice10.mc.protocol.packet.ingame.clientbound.scoreboard;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

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
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
        helper.writeString(out, this.owner);
        helper.writeNullable(out, this.objective, helper::writeString);
    }
}
