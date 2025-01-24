package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.entity.player;

import io.netty.buffer.ByteBuf;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@NoArgsConstructor
public class ClientboundPlayerCombatEnterPacket implements MinecraftPacket {

    public ClientboundPlayerCombatEnterPacket(ByteBuf in) {
        // no-op
    }

    @Override
    public void serialize(ByteBuf out) {
        // no-op
    }
}
