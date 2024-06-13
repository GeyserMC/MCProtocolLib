package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.data.game.entity.player.PlayerSpawnInfo;

@Data
@With
@AllArgsConstructor
public class ClientboundRespawnPacket implements MinecraftPacket {
    private static final byte KEEP_ATTRIBUTE_MODIFIERS = 1;
    private static final byte KEEP_ENTITY_DATA = 2;

    private final PlayerSpawnInfo commonPlayerSpawnInfo;
    // The following two are the dataToKeep byte
    private final boolean keepMetadata;
    private final boolean keepAttributeModifiers;

    public ClientboundRespawnPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.commonPlayerSpawnInfo = helper.readPlayerSpawnInfo(in);
        byte dataToKeep = in.readByte();
        this.keepAttributeModifiers = (dataToKeep & KEEP_ATTRIBUTE_MODIFIERS) != 0;
        this.keepMetadata = (dataToKeep & KEEP_ENTITY_DATA) != 0;
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writePlayerSpawnInfo(out, this.commonPlayerSpawnInfo);
        byte dataToKeep = 0;
        if (this.keepMetadata) {
            dataToKeep += KEEP_ENTITY_DATA;
        }
        if (this.keepAttributeModifiers) {
            dataToKeep += KEEP_ATTRIBUTE_MODIFIERS;
        }
        out.writeByte(dataToKeep);
    }
}
