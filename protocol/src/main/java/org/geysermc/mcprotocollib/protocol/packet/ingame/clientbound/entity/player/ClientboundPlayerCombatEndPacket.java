package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.entity.player;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ClientboundPlayerCombatEndPacket implements MinecraftPacket {
    private final int duration;

    public ClientboundPlayerCombatEndPacket(MinecraftByteBuf buf) {
        this.duration = buf.readVarInt();
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeVarInt(this.duration);
    }
}
