package org.geysermc.mcprotocollib.protocol.data.game.chat;

import org.cloudburstmc.nbt.NbtMap;

public record ChatType(ChatTypeDecoration chat, ChatTypeDecoration narration) {
    public record ChatTypeDecoration(String translationKey, int[] parameters, NbtMap style) {
    }
}
