package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.level;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.cloudburstmc.math.vector.Vector3i;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;
import org.geysermc.mcprotocollib.protocol.data.game.entity.player.BlockBreakStage;

@Data
@With
@AllArgsConstructor
public class ClientboundBlockDestructionPacket implements MinecraftPacket {
    private final int breakerEntityId;
    private final @NonNull Vector3i position;
    private final @NonNull BlockBreakStage stage;

    public ClientboundBlockDestructionPacket(ByteBuf in) {
        this.breakerEntityId = MinecraftTypes.readVarInt(in);
        this.position = MinecraftTypes.readPosition(in);
        this.stage = MinecraftTypes.readBlockBreakStage(in);
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeVarInt(out, this.breakerEntityId);
        MinecraftTypes.writePosition(out, this.position);
        MinecraftTypes.writeBlockBreakStage(out, this.stage);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
