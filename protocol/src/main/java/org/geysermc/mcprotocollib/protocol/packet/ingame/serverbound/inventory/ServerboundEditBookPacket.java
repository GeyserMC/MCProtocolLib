package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.inventory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

import java.util.ArrayList;
import java.util.List;

@Data
@With
@AllArgsConstructor
public class ServerboundEditBookPacket implements MinecraftPacket {
    private final int slot;
    private final List<String> pages;
    private final @Nullable String title;

    public ServerboundEditBookPacket(MinecraftByteBuf buf) {
        this.slot = buf.readVarInt();
        this.pages = new ArrayList<>();
        int pagesSize = buf.readVarInt();
        for (int i = 0; i < pagesSize; i++) {
            this.pages.add(buf.readString());
        }
        if (buf.readBoolean()) {
            this.title = buf.readString();
        } else {
            this.title = null;
        }
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeVarInt(slot);
        buf.writeVarInt(this.pages.size());
        for (String page : this.pages) {
            buf.writeString(page);
        }
        buf.writeBoolean(this.title != null);
        if (this.title != null) {
            buf.writeString(title);
        }
    }
}
