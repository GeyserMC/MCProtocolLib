package com.github.steveice10.mc.protocol.data.game.chat;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Objects;

public record MessageSignature(int id, byte @Nullable [] messageSignature) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MessageSignature that)) return false;
        return id == that.id && Arrays.equals(messageSignature, that.messageSignature);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id);
        result = 31 * result + Arrays.hashCode(messageSignature);
        return result;
    }
}
