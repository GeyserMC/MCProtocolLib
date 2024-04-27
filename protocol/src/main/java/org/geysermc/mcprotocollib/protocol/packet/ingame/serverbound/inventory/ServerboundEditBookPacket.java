package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.inventory;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
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

    public ServerboundEditBookPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.slot = helper.readVarInt(in);
        this.pages = new ArrayList<>();
        int pagesSize = helper.readVarInt(in);
        for (int i = 0; i < pagesSize; i++) {
            this.pages.add(helper.readString(in));
        }
        if (in.readBoolean()) {
            this.title = helper.readString(in);
        } else {
            this.title = null;
        }
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writeVarInt(out, slot);
        helper.writeVarInt(out, this.pages.size());
        for (String page : this.pages) {
            helper.writeString(out, page);
        }
        out.writeBoolean(this.title != null);
        if (this.title != null) {
            helper.writeString(out, title);
        }
    }
}
