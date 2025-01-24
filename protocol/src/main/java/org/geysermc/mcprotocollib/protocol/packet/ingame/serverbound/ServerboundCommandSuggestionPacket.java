package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;

@Data
@With
@AllArgsConstructor
public class ServerboundCommandSuggestionPacket implements MinecraftPacket {
    private final int transactionId;
    private final @NonNull String text;

    public ServerboundCommandSuggestionPacket(ByteBuf in) {
        this.transactionId = MinecraftTypes.readVarInt(in);
        this.text = MinecraftTypes.readString(in);
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeVarInt(out, this.transactionId);
        MinecraftTypes.writeString(out, this.text);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
