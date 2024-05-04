package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.data.game.entity.Effect;

@Data
@With
@AllArgsConstructor
public class ClientboundRemoveMobEffectPacket implements MinecraftPacket {
    private final int entityId;
    private final @NonNull Effect effect;

    public ClientboundRemoveMobEffectPacket(MinecraftByteBuf buf) {
        this.entityId = buf.readVarInt();
        this.effect = buf.readEffect();
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeVarInt(this.entityId);
        buf.writeEffect(this.effect);
    }
}
