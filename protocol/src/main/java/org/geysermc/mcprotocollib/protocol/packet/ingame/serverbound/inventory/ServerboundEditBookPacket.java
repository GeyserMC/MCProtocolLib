package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.inventory;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;

import java.util.List;

@Data
@With
@AllArgsConstructor
public class ServerboundEditBookPacket implements MinecraftPacket {
    private final int slot;
    private final List<String> pages;
    private final @Nullable String title;

    public ServerboundEditBookPacket(ByteBuf in) {
        this.slot = MinecraftTypes.readVarInt(in);
        this.pages = MinecraftTypes.readList(in, MinecraftTypes::readString);
        this.title = MinecraftTypes.readNullable(in, MinecraftTypes::readString);
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeVarInt(out, slot);
        MinecraftTypes.writeList(out, pages, MinecraftTypes::writeString);
        MinecraftTypes.writeNullable(out, title, MinecraftTypes::writeString);
    }
}
