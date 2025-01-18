package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.level;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;
import org.geysermc.mcprotocollib.protocol.data.game.level.block.BlockChangeEntry;

@Data
@With
@AllArgsConstructor
public class ClientboundBlockUpdatePacket implements MinecraftPacket {
    private final @NonNull BlockChangeEntry entry;

    public ClientboundBlockUpdatePacket(ByteBuf in) {
        this.entry = new BlockChangeEntry(MinecraftTypes.readPosition(in), MinecraftTypes.readVarInt(in));
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writePosition(out, this.entry.getPosition());
        MinecraftTypes.writeVarInt(out, this.entry.getBlock());
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
