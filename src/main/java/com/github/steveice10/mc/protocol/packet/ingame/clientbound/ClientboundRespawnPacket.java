package com.github.steveice10.mc.protocol.packet.ingame.clientbound;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import com.github.steveice10.mc.protocol.data.game.entity.player.PlayerSpawnInfo;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;

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

    public ClientboundRespawnPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.commonPlayerSpawnInfo = helper.readPlayerSpawnInfo(in);
        byte dataToKeep = in.readByte();
        this.keepAttributes = (dataToKeep & KEEP_ATTRIBUTES) != 0;
        this.keepMetadata = (dataToKeep & KEEP_ENTITY_DATA) != 0;
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writePlayerSpawnInfo(out, this.commonPlayerSpawnInfo);
        byte dataToKeep = 0;
        if (this.keepMetadata) {
            dataToKeep += KEEP_ENTITY_DATA;
        }
        if (this.keepAttributes) {
            dataToKeep += KEEP_ATTRIBUTES;
        }
        out.writeByte(dataToKeep);
    }
}
