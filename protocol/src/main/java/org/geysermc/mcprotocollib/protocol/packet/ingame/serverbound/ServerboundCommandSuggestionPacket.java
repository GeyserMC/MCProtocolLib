package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ServerboundCommandSuggestionPacket implements MinecraftPacket {
    private final int transactionId;
    private final @NonNull String text;

    public ServerboundCommandSuggestionPacket(MinecraftByteBuf buf) {
        this.transactionId = buf.readVarInt();
        this.text = buf.readString();
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeVarInt(this.transactionId);
        buf.writeString(this.text);
    }
}
