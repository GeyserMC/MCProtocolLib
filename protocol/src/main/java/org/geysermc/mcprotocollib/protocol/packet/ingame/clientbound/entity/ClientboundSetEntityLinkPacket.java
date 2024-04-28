package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.entity;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ClientboundSetEntityLinkPacket implements MinecraftPacket {
    private final int entityId;
    private final int attachedToId;

    public ClientboundSetEntityLinkPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.entityId = in.readInt();
        this.attachedToId = in.readInt();
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        out.writeInt(this.entityId);
        out.writeInt(this.attachedToId);
    }
}
