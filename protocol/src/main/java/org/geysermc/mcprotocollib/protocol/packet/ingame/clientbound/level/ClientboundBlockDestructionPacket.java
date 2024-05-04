package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.level;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.cloudburstmc.math.vector.Vector3i;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.data.game.entity.player.BlockBreakStage;

@Data
@With
@AllArgsConstructor
public class ClientboundBlockDestructionPacket implements MinecraftPacket {
    private final int breakerEntityId;
    private final @NonNull Vector3i position;
    private final @NonNull BlockBreakStage stage;

    public ClientboundBlockDestructionPacket(MinecraftByteBuf buf) {
        this.breakerEntityId = buf.readVarInt();
        this.position = buf.readPosition();
        this.stage = buf.readBlockBreakStage();
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeVarInt(this.breakerEntityId);
        buf.writePosition(this.position);
        buf.writeBlockBreakStage(this.stage);
    }
}
