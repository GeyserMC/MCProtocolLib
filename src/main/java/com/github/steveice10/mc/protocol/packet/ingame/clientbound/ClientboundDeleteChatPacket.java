package com.github.steveice10.mc.protocol.packet.ingame.clientbound;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import com.github.steveice10.mc.protocol.data.game.chat.MessageSignature;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;

import java.io.IOException;

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
