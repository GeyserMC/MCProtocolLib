package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.data.game.entity.player.PlayerSpawnInfo;

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
    private final boolean enforcesSecureChat;

    public ClientboundLoginPacket(MinecraftByteBuf buf) {
        this.entityId = buf.readInt();
        this.hardcore = buf.readBoolean();
        int worldCount = buf.readVarInt();
        this.worldNames = new String[worldCount];
        for (int i = 0; i < worldCount; i++) {
            this.worldNames[i] = buf.readString();
        }
        this.maxPlayers = buf.readVarInt();
        this.viewDistance = buf.readVarInt();
        this.simulationDistance = buf.readVarInt();
        this.reducedDebugInfo = buf.readBoolean();
        this.enableRespawnScreen = buf.readBoolean();
        this.doLimitedCrafting = buf.readBoolean();
        this.commonPlayerSpawnInfo = buf.readPlayerSpawnInfo();
        this.enforcesSecureChat = buf.readBoolean();
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeInt(this.entityId);
        buf.writeBoolean(this.hardcore);
        buf.writeVarInt(this.worldNames.length);
        for (String worldName : this.worldNames) {
            buf.writeString(worldName);
        }
        buf.writeVarInt(this.maxPlayers);
        buf.writeVarInt(this.viewDistance);
        buf.writeVarInt(this.simulationDistance);
        buf.writeBoolean(this.reducedDebugInfo);
        buf.writeBoolean(this.enableRespawnScreen);
        buf.writeBoolean(this.doLimitedCrafting);
        buf.writePlayerSpawnInfo(this.commonPlayerSpawnInfo);
        buf.writeBoolean(this.enforcesSecureChat);
    }
}
