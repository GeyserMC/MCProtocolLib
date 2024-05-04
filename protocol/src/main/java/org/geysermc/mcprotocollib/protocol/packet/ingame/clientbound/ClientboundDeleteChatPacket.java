package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.data.game.chat.MessageSignature;

@Data
@With
@AllArgsConstructor
public class ClientboundDeleteChatPacket implements MinecraftPacket {
    private final MessageSignature messageSignature;

    public ClientboundDeleteChatPacket(MinecraftByteBuf buf) {
        this.messageSignature = MessageSignature.read(buf);
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeVarInt(this.messageSignature.getId() + 1);
        if (this.messageSignature.getMessageSignature() != null) {
            buf.writeBytes(messageSignature.getMessageSignature());
        }
    }
}
