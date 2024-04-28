package org.geysermc.mcprotocollib.protocol.packet.common.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
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

    public ClientboundResourcePackPopPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.id = helper.readNullable(in, helper::readUUID);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writeNullable(out, this.id, helper::writeUUID);
    }
}
