package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.data.game.chat.ChatCompletionAction;

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
