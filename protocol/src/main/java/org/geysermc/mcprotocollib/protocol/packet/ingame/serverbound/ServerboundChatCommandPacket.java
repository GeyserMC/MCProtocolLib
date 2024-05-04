package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ServerboundChatCommandPacket implements MinecraftPacket {
    private final String command;

    public ServerboundChatCommandPacket(MinecraftByteBuf buf) {
        this.command = buf.readString();
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeString(this.command);
    }
}
