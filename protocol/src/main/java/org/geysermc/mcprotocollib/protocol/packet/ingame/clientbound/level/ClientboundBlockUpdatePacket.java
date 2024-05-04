package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.level;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.data.game.level.block.BlockChangeEntry;

@Data
@With
@AllArgsConstructor
public class ClientboundBlockUpdatePacket implements MinecraftPacket {
    private final @NonNull BlockChangeEntry entry;

    public ClientboundBlockUpdatePacket(MinecraftByteBuf buf) {
        this.entry = new BlockChangeEntry(buf.readPosition(), buf.readVarInt());
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writePosition(this.entry.getPosition());
        buf.writeVarInt(this.entry.getBlock());
    }
}
