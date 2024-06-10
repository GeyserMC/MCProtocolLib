package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import net.kyori.adventure.key.Key;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
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

    public ClientboundLoginPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.entityId = in.readInt();
        this.hardcore = in.readBoolean();
        int worldCount = helper.readVarInt(in);
        this.worldNames = new Key[worldCount];
        for (int i = 0; i < worldCount; i++) {
            this.worldNames[i] = helper.readResourceLocation(in);
        }
        this.maxPlayers = helper.readVarInt(in);
        this.viewDistance = helper.readVarInt(in);
        this.simulationDistance = helper.readVarInt(in);
        this.reducedDebugInfo = in.readBoolean();
        this.enableRespawnScreen = in.readBoolean();
        this.doLimitedCrafting = in.readBoolean();
        this.commonPlayerSpawnInfo = helper.readPlayerSpawnInfo(in);
        this.enforcesSecureChat = in.readBoolean();
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        out.writeInt(this.entityId);
        out.writeBoolean(this.hardcore);
        helper.writeVarInt(out, this.worldNames.length);
        for (Key worldName : this.worldNames) {
            helper.writeResourceLocation(out, worldName);
        }
        helper.writeVarInt(out, this.maxPlayers);
        helper.writeVarInt(out, this.viewDistance);
        helper.writeVarInt(out, this.simulationDistance);
        out.writeBoolean(this.reducedDebugInfo);
        out.writeBoolean(this.enableRespawnScreen);
        out.writeBoolean(this.doLimitedCrafting);
        helper.writePlayerSpawnInfo(out, this.commonPlayerSpawnInfo);
        out.writeBoolean(this.enforcesSecureChat);
    }
}
