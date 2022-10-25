package com.github.steveice10.mc.protocol.packet.ingame.clientbound;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import com.github.steveice10.mc.protocol.data.game.chat.ChatCompletionAction;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;

@Data
@With
@AllArgsConstructor
public class ClientboundCustomChatCompletionsPacket implements MinecraftPacket {
    private final ChatCompletionAction action;
    private final String[] entries;

    public ClientboundCustomChatCompletionsPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.action = ChatCompletionAction.from(helper.readVarInt(in));
        this.entries = new String[helper.readVarInt(in)];
        for (int i = 0; i < this.entries.length; i++) {
            this.entries[i] = helper.readString(in);
        }
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writeVarInt(out, this.action.ordinal());
        helper.writeVarInt(out, this.entries.length);
        for (String entry : this.entries) {
            helper.writeString(out, entry);
        }
    }
}
