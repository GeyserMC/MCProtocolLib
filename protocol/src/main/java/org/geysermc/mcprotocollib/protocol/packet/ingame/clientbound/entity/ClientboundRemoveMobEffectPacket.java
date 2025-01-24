package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.entity;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;
import org.geysermc.mcprotocollib.protocol.data.game.entity.Effect;

@Data
@With
@AllArgsConstructor
public class ClientboundRemoveMobEffectPacket implements MinecraftPacket {
    private final int entityId;
    private final @NonNull Effect effect;

    public ClientboundRemoveMobEffectPacket(ByteBuf in) {
        this.entityId = MinecraftTypes.readVarInt(in);
        this.effect = MinecraftTypes.readEffect(in);
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeVarInt(out, this.entityId);
        MinecraftTypes.writeEffect(out, this.effect);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
