package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.level;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.cloudburstmc.math.vector.Vector3i;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.data.game.entity.player.BlockBreakStage;

@Data
@With
@AllArgsConstructor
public class ClientboundBlockDestructionPacket implements MinecraftPacket {
    private final int breakerEntityId;
    private final @NonNull Vector3i position;
    private final @NonNull BlockBreakStage stage;

    public ClientboundBlockDestructionPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.breakerEntityId = helper.readVarInt(in);
        this.position = helper.readPosition(in);
        this.stage = helper.readBlockBreakStage(in);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writeVarInt(out, this.breakerEntityId);
        helper.writePosition(out, this.position);
        helper.writeBlockBreakStage(out, this.stage);
    }
}
