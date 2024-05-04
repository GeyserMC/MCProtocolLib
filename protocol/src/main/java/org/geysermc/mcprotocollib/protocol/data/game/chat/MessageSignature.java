package org.geysermc.mcprotocollib.protocol.data.game.chat;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.checkerframework.checker.nullness.qual.Nullable;

@Data
@AllArgsConstructor
public class MessageSignature {
    private final int id;
    private final byte @Nullable [] messageSignature;

    public static MessageSignature read(MinecraftByteBuf in) {
        int id = in.readVarInt() - 1;
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
