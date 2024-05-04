package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.data.game.chat.ChatCompletionAction;

@Data
@With
@AllArgsConstructor
public class ClientboundCustomChatCompletionsPacket implements MinecraftPacket {
    private final ChatCompletionAction action;
    private final String[] entries;

    public ClientboundCustomChatCompletionsPacket(MinecraftByteBuf buf) {
        this.action = ChatCompletionAction.from(buf.readVarInt());
        this.entries = new String[buf.readVarInt()];
        for (int i = 0; i < this.entries.length; i++) {
            this.entries[i] = buf.readString();
        }
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeVarInt(this.action.ordinal());
        buf.writeVarInt(this.entries.length);
        for (String entry : this.entries) {
            buf.writeString(entry);
        }
    }
}
