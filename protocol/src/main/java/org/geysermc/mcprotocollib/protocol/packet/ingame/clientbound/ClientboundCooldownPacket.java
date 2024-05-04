package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ClientboundCooldownPacket implements MinecraftPacket {
    private final int itemId;
    private final int cooldownTicks;

    public ClientboundCooldownPacket(MinecraftByteBuf buf) {
        this.itemId = buf.readVarInt();
        this.cooldownTicks = buf.readVarInt();
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeVarInt(this.itemId);
        buf.writeVarInt(this.cooldownTicks);
    }
}
