package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.inventory;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

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
        this.pages = helper.readList(in, helper::readString);
        this.title = helper.readNullable(in, helper::readString);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writeVarInt(out, slot);
        helper.writeList(out, pages, helper::writeString);
        helper.writeNullable(out, title, helper::writeString);
    }
}
