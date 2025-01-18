package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.entity;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;

@Data
@With
@AllArgsConstructor
public class ClientboundTakeItemEntityPacket implements MinecraftPacket {
    private final int collectedEntityId;
    private final int collectorEntityId;
    private final int itemCount;

    public ClientboundTakeItemEntityPacket(ByteBuf in) {
        this.collectedEntityId = MinecraftTypes.readVarInt(in);
        this.collectorEntityId = MinecraftTypes.readVarInt(in);
        this.itemCount = MinecraftTypes.readVarInt(in);
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeVarInt(out, this.collectedEntityId);
        MinecraftTypes.writeVarInt(out, this.collectorEntityId);
        MinecraftTypes.writeVarInt(out, this.itemCount);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
