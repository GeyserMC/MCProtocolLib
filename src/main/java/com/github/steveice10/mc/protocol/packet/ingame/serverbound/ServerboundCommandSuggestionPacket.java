package com.github.steveice10.mc.protocol.packet.ingame.serverbound;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;

import java.io.IOException;

@Data
@With
@AllArgsConstructor
public class ServerboundCommandSuggestionPacket implements MinecraftPacket {
    private final int transactionId;
    private final @NonNull String text;

    public ServerboundCommandSuggestionPacket(ByteBuf in, MinecraftCodecHelper helper) throws IOException {
        this.transactionId = helper.readVarInt(in);
        this.text = helper.readString(in);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
        helper.writeVarInt(out, this.transactionId);
        helper.writeString(out, this.text);
    }
}
