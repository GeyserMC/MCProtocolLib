package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.level;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;

@Data
@With
@AllArgsConstructor
public class ServerboundEntityTagQuery implements MinecraftPacket {
    private final int transactionId;
    private final int entityId;

    public ServerboundEntityTagQuery(ByteBuf in) {
        this.transactionId = MinecraftTypes.readVarInt(in);
        this.entityId = MinecraftTypes.readVarInt(in);
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeVarInt(out, this.transactionId);
        MinecraftTypes.writeVarInt(out, this.entityId);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
