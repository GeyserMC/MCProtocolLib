package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import net.kyori.adventure.key.Key;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;
import org.geysermc.mcprotocollib.protocol.data.game.entity.player.PlayerSpawnInfo;

@Data
@With
@AllArgsConstructor
public class ClientboundLoginPacket implements MinecraftPacket {
    private final int entityId;
    private final boolean hardcore;
    private final @NonNull Key[] worldNames;
    private final int maxPlayers;
    private final int viewDistance;
    private final int simulationDistance;
    private final boolean reducedDebugInfo;
    private final boolean enableRespawnScreen;
    private final boolean doLimitedCrafting;
    private final PlayerSpawnInfo commonPlayerSpawnInfo;
    private final boolean enforcesSecureChat;

    public ClientboundLoginPacket(ByteBuf in) {
        this.entityId = in.readInt();
        this.hardcore = in.readBoolean();
        int worldCount = MinecraftTypes.readVarInt(in);
        this.worldNames = new Key[worldCount];
        for (int i = 0; i < worldCount; i++) {
            this.worldNames[i] = MinecraftTypes.readResourceLocation(in);
        }
        this.maxPlayers = MinecraftTypes.readVarInt(in);
        this.viewDistance = MinecraftTypes.readVarInt(in);
        this.simulationDistance = MinecraftTypes.readVarInt(in);
        this.reducedDebugInfo = in.readBoolean();
        this.enableRespawnScreen = in.readBoolean();
        this.doLimitedCrafting = in.readBoolean();
        this.commonPlayerSpawnInfo = MinecraftTypes.readPlayerSpawnInfo(in);
        this.enforcesSecureChat = in.readBoolean();
    }

    @Override
    public void serialize(ByteBuf out) {
        out.writeInt(this.entityId);
        out.writeBoolean(this.hardcore);
        MinecraftTypes.writeVarInt(out, this.worldNames.length);
        for (Key worldName : this.worldNames) {
            MinecraftTypes.writeResourceLocation(out, worldName);
        }
        MinecraftTypes.writeVarInt(out, this.maxPlayers);
        MinecraftTypes.writeVarInt(out, this.viewDistance);
        MinecraftTypes.writeVarInt(out, this.simulationDistance);
        out.writeBoolean(this.reducedDebugInfo);
        out.writeBoolean(this.enableRespawnScreen);
        out.writeBoolean(this.doLimitedCrafting);
        MinecraftTypes.writePlayerSpawnInfo(out, this.commonPlayerSpawnInfo);
        out.writeBoolean(this.enforcesSecureChat);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
