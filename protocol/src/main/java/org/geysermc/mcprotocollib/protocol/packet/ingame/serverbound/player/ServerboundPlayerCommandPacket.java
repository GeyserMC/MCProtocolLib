package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.player;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
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

    public ServerboundPlayerCommandPacket(MinecraftByteBuf buf) {
        this.entityId = buf.readVarInt();
        this.state = PlayerState.from(buf.readVarInt());
        this.jumpBoost = buf.readVarInt();
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeVarInt(this.entityId);
        buf.writeVarInt(this.state.ordinal());
        buf.writeVarInt(this.jumpBoost);
    }
}
