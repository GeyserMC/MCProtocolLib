package org.geysermc.mcprotocollib.protocol.packet.common.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;

import java.util.UUID;

@Data
@With
@AllArgsConstructor
public class ClientboundResourcePackPopPacket implements MinecraftPacket {

    /**
     * When null, indicates that all packs should be popped.
     */
    private final @Nullable UUID id;

    public ClientboundResourcePackPopPacket(ByteBuf in) {
        this.id = MinecraftTypes.readNullable(in, MinecraftTypes::readUUID);
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeNullable(out, this.id, MinecraftTypes::writeUUID);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
