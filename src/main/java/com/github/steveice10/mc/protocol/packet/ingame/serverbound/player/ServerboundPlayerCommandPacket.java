package com.github.steveice10.mc.protocol.packet.ingame.serverbound.player;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import com.github.steveice10.mc.protocol.data.game.entity.player.PlayerState;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;

import java.io.IOException;

@Data
@With
@AllArgsConstructor
public class ServerboundPlayerCommandPacket implements MinecraftPacket {
    private final int entityId;
    private final @NonNull PlayerState state;
    private final int jumpBoost;

    public ServerboundPlayerCommandPacket(int entityId, PlayerState state) {
        this(entityId, state, 0);
    }

    public ServerboundPlayerCommandPacket(ByteBuf in, MinecraftCodecHelper helper) throws IOException {
        this.entityId = helper.readVarInt(in);
        this.state = PlayerState.from(helper.readVarInt(in));
        this.jumpBoost = helper.readVarInt(in);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
        helper.writeVarInt(out, this.entityId);
        helper.writeVarInt(out, this.state.ordinal());
        helper.writeVarInt(out, this.jumpBoost);
    }
}
