package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.entity.player;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import net.kyori.adventure.text.Component;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ClientboundPlayerCombatKillPacket implements MinecraftPacket {
    private final int playerId;
    private final Component message;

    public ClientboundPlayerCombatKillPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.playerId = helper.readVarInt(in);
        this.message = helper.readComponent(in);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writeVarInt(out, this.playerId);
        helper.writeComponent(out, this.message);
    }
}
