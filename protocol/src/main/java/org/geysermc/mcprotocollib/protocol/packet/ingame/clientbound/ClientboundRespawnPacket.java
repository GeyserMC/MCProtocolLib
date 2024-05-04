package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.data.game.entity.player.PlayerSpawnInfo;

@Data
@With
@AllArgsConstructor
public class ClientboundRespawnPacket implements MinecraftPacket {
    private static final byte KEEP_ATTRIBUTES = 1;
    private static final byte KEEP_ENTITY_DATA = 2;

    private final PlayerSpawnInfo commonPlayerSpawnInfo;
    // The following two are the dataToKeep byte
    private final boolean keepMetadata;
    private final boolean keepAttributes;

    public ClientboundRespawnPacket(MinecraftByteBuf buf) {
        this.commonPlayerSpawnInfo = buf.readPlayerSpawnInfo();
        byte dataToKeep = buf.readByte();
        this.keepAttributes = (dataToKeep & KEEP_ATTRIBUTES) != 0;
        this.keepMetadata = (dataToKeep & KEEP_ENTITY_DATA) != 0;
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writePlayerSpawnInfo(this.commonPlayerSpawnInfo);
        byte dataToKeep = 0;
        if (this.keepMetadata) {
            dataToKeep += KEEP_ENTITY_DATA;
        }
        if (this.keepAttributes) {
            dataToKeep += KEEP_ATTRIBUTES;
        }
        buf.writeByte(dataToKeep);
    }
}
