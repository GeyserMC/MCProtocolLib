package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.entity.player;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;

@Data
@With
@AllArgsConstructor
public class ClientboundBlockChangedAckPacket implements MinecraftPacket {
    private final int sequence;

    public ClientboundBlockChangedAckPacket(ByteBuf in) {
        this.sequence = MinecraftTypes.readVarInt(in);
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeVarInt(out, this.sequence);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
