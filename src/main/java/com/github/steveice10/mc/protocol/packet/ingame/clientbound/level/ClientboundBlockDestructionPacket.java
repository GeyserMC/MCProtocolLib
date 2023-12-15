package com.github.steveice10.mc.protocol.packet.ingame.clientbound.level;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import com.github.steveice10.mc.protocol.data.game.entity.player.BlockBreakStage;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.cloudburstmc.math.vector.Vector3i;

import java.io.IOException;

@Data
@With
@AllArgsConstructor
public class ClientboundBlockDestructionPacket implements MinecraftPacket {
    private final int breakerEntityId;
    private final @NonNull Vector3i position;
    private final @NonNull BlockBreakStage stage;

    public ClientboundBlockDestructionPacket(ByteBuf in, MinecraftCodecHelper helper) throws IOException {
        this.breakerEntityId = helper.readVarInt(in);
        this.position = helper.readPosition(in);
        this.stage = helper.readBlockBreakStage(in);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
        helper.writeVarInt(out, this.breakerEntityId);
        helper.writePosition(out, this.position);
        helper.writeBlockBreakStage(out, this.stage);
    }
}
