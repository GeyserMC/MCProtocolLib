package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.player;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.data.game.entity.player.PlayerState;

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

    public ServerboundPlayerCommandPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.entityId = helper.readVarInt(in);
        this.state = PlayerState.from(helper.readVarInt(in));
        this.jumpBoost = helper.readVarInt(in);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writeVarInt(out, this.entityId);
        helper.writeVarInt(out, this.state.ordinal());
        helper.writeVarInt(out, this.jumpBoost);
    }
}
