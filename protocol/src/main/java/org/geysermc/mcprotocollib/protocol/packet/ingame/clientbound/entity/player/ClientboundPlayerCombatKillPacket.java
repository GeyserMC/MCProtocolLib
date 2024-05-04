package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.entity.player;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import net.kyori.adventure.text.Component;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ClientboundPlayerCombatKillPacket implements MinecraftPacket {
    private final int playerId;
    private final Component message;

    public ClientboundPlayerCombatKillPacket(MinecraftByteBuf buf) {
        this.playerId = buf.readVarInt();
        this.message = buf.readComponent();
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeVarInt(this.playerId);
        buf.writeComponent(this.message);
    }
}
