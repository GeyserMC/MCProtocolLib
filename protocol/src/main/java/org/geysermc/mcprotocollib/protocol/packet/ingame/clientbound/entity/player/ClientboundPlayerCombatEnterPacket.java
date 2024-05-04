package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.entity.player;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@NoArgsConstructor
public class ClientboundPlayerCombatEnterPacket implements MinecraftPacket {

    public ClientboundPlayerCombatEnterPacket(MinecraftByteBuf buf) {
        // no-op
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        // no-op
    }
}
