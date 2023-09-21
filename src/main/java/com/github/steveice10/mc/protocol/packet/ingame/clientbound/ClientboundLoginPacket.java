package com.github.steveice10.mc.protocol.packet.ingame.clientbound;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.GlobalPos;
import com.github.steveice10.mc.protocol.data.game.entity.player.GameMode;
import com.github.steveice10.mc.protocol.data.game.entity.player.PlayerSpawnInfo;
import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

@Data
@With
@AllArgsConstructor
public class ClientboundLoginPacket implements MinecraftPacket {
    private final int entityId;
    private final boolean hardcore;
    private final @NonNull String[] worldNames;
    private final int maxPlayers;
    private final int viewDistance;
    private final int simulationDistance;
    private final boolean reducedDebugInfo;
    private final boolean enableRespawnScreen;
    private final boolean doLimitedCrafting;
    private final PlayerSpawnInfo commonPlayerSpawnInfo;

    public ClientboundLoginPacket(ByteBuf in, MinecraftCodecHelper helper) throws IOException {
        this.entityId = in.readInt();
        this.hardcore = in.readBoolean();
        int worldCount = helper.readVarInt(in);
        this.worldNames = new String[worldCount];
        for (int i = 0; i < worldCount; i++) {
            this.worldNames[i] = helper.readString(in);
        }
        this.maxPlayers = helper.readVarInt(in);
        this.viewDistance = helper.readVarInt(in);
        this.simulationDistance = helper.readVarInt(in);
        this.reducedDebugInfo = in.readBoolean();
        this.enableRespawnScreen = in.readBoolean();
        this.doLimitedCrafting = in.readBoolean();
        this.commonPlayerSpawnInfo = helper.readPlayerSpawnInfo(in);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
        out.writeInt(this.entityId);
        out.writeBoolean(this.hardcore);
        helper.writeVarInt(out, this.worldNames.length);
        for (String worldName : this.worldNames) {
            helper.writeString(out, worldName);
        }
        helper.writeVarInt(out, this.maxPlayers);
        helper.writeVarInt(out, this.viewDistance);
        helper.writeVarInt(out, this.simulationDistance);
        out.writeBoolean(this.reducedDebugInfo);
        out.writeBoolean(this.enableRespawnScreen);
        out.writeBoolean(this.doLimitedCrafting);
        helper.writePlayerSpawnInfo(out, this.commonPlayerSpawnInfo);
    }
}
