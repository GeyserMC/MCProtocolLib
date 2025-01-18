package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;
import org.geysermc.mcprotocollib.protocol.data.game.chat.ChatCompletionAction;

@Data
@With
@AllArgsConstructor
public class ClientboundCustomChatCompletionsPacket implements MinecraftPacket {
    private final ChatCompletionAction action;
    private final String[] entries;

    public ClientboundCustomChatCompletionsPacket(ByteBuf in) {
        this.action = ChatCompletionAction.from(MinecraftTypes.readVarInt(in));
        this.entries = new String[MinecraftTypes.readVarInt(in)];
        for (int i = 0; i < this.entries.length; i++) {
            this.entries[i] = MinecraftTypes.readString(in);
        }
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeVarInt(out, this.action.ordinal());
        MinecraftTypes.writeVarInt(out, this.entries.length);
        for (String entry : this.entries) {
            MinecraftTypes.writeString(out, entry);
        }
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
