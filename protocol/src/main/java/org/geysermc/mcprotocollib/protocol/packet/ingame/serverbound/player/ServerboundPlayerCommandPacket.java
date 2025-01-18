package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.player;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;
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

    public ServerboundPlayerCommandPacket(ByteBuf in) {
        this.entityId = MinecraftTypes.readVarInt(in);
        this.state = PlayerState.from(MinecraftTypes.readVarInt(in));
        this.jumpBoost = MinecraftTypes.readVarInt(in);
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeVarInt(out, this.entityId);
        MinecraftTypes.writeVarInt(out, this.state.ordinal());
        MinecraftTypes.writeVarInt(out, this.jumpBoost);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
