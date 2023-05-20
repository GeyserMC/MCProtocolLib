package com.github.steveice10.mc.protocol.packet.ingame.clientbound.entity.player;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import net.kyori.adventure.text.Component;

import java.io.IOException;

@Data
@With
@AllArgsConstructor
public class ClientboundPlayerCombatKillPacket implements MinecraftPacket {
    private final int playerId;
    private final Component message;

    public ClientboundPlayerCombatKillPacket(ByteBuf in, MinecraftCodecHelper helper) throws IOException {
        this.playerId = helper.readVarInt(in);
        this.message = helper.readComponent(in);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
        helper.writeVarInt(out, this.playerId);
        helper.writeComponent(out, this.message);
    }
}
