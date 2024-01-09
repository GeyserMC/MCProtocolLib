package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.entity.player;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import io.netty.buffer.ByteBuf;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

@Data
@With
@NoArgsConstructor
public class ClientboundPlayerCombatEnterPacket implements MinecraftPacket {

    public ClientboundPlayerCombatEnterPacket(ByteBuf in, MinecraftCodecHelper helper) {
        // no-op
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        // no-op
    }
}
