package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.data.game.chat.MessageSignature;

@Data
@With
@AllArgsConstructor
public class ClientboundDeleteChatPacket implements MinecraftPacket {
    private final MessageSignature messageSignature;

    public ClientboundDeleteChatPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.messageSignature = MessageSignature.read(in, helper);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writeVarInt(out, this.messageSignature.getId() + 1);
        if (this.messageSignature.getMessageSignature() != null) {
            out.writeBytes(messageSignature.getMessageSignature());
        }
    }
}
