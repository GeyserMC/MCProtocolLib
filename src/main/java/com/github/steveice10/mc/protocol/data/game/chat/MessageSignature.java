package com.github.steveice10.mc.protocol.data.game.chat;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.Nullable;

@Data
@AllArgsConstructor
public class MessageSignature {
    private final int id;
    private final byte @Nullable[] messageSignature;

    public static MessageSignature read(ByteBuf in, MinecraftCodecHelper helper) {
        int id = helper.readVarInt(in) - 1;
        byte[] messageSignature;
        if (id == -1) {
            messageSignature = new byte[256];
            in.readBytes(messageSignature);
        } else {
            messageSignature = null;
        }
        return new MessageSignature(id, messageSignature);
    }
}
