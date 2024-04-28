package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.level;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.cloudburstmc.math.vector.Vector3i;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ServerboundBlockEntityTagQueryPacket implements MinecraftPacket {
    private final int transactionId;
    private final @NonNull Vector3i position;

    public ServerboundBlockEntityTagQueryPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.transactionId = helper.readVarInt(in);
        this.position = helper.readPosition(in);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writeVarInt(out, this.transactionId);
        helper.writePosition(out, this.position);
    }
}
