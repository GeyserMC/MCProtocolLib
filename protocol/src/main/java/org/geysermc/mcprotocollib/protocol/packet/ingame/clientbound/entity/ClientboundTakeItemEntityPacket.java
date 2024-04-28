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
public class ClientboundTakeItemEntityPacket implements MinecraftPacket {
    private final int collectedEntityId;
    private final int collectorEntityId;
    private final int itemCount;

    public ClientboundTakeItemEntityPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.collectedEntityId = helper.readVarInt(in);
        this.collectorEntityId = helper.readVarInt(in);
        this.itemCount = helper.readVarInt(in);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writeVarInt(out, this.collectedEntityId);
        helper.writeVarInt(out, this.collectorEntityId);
        helper.writeVarInt(out, this.itemCount);
    }
}
