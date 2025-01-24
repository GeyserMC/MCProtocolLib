package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.entity.player;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import net.kyori.adventure.text.Component;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;

@Data
@With
@AllArgsConstructor
public class ClientboundPlayerCombatKillPacket implements MinecraftPacket {
    private final int playerId;
    private final Component message;

    public ClientboundPlayerCombatKillPacket(ByteBuf in) {
        this.playerId = MinecraftTypes.readVarInt(in);
        this.message = MinecraftTypes.readComponent(in);
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeVarInt(out, this.playerId);
        MinecraftTypes.writeComponent(out, this.message);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
