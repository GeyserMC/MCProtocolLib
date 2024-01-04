package org.geysermc.mc.protocol.packet.ingame.clientbound.entity.player;

import org.geysermc.mc.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mc.protocol.codec.MinecraftPacket;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;

@Data
@With
@AllArgsConstructor
public class ClientboundPlayerCombatEndPacket implements MinecraftPacket {
    private final int duration;

    public ClientboundPlayerCombatEndPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.duration = helper.readVarInt(in);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writeVarInt(out, this.duration);
    }
}
