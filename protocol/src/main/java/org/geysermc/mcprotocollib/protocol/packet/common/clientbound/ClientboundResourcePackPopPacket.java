package org.geysermc.mcprotocollib.protocol.packet.common.clientbound;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

import java.util.UUID;

@Data
@With
@AllArgsConstructor
public class ClientboundResourcePackPopPacket implements MinecraftPacket {

    /**
     * When null, indicates that all packs should be popped.
     */
    private final @Nullable UUID id;

    public ClientboundResourcePackPopPacket(MinecraftByteBuf buf) {
        this.id = buf.readNullable(buf::readUUID);
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeNullable(this.id, buf::writeUUID);
    }
}
