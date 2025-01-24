package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;

@Data
@With
@AllArgsConstructor
public class ClientboundSelectAdvancementsTabPacket implements MinecraftPacket {
    private final @Nullable String tabId;

    public ClientboundSelectAdvancementsTabPacket(ByteBuf in) {
        this.tabId = MinecraftTypes.readNullable(in, MinecraftTypes::readString);
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeNullable(out, this.tabId, MinecraftTypes::writeString);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
