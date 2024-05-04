package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.entity;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ClientboundSetPassengersPacket implements MinecraftPacket {
    private final int entityId;
    private final int @NonNull [] passengerIds;

    public ClientboundSetPassengersPacket(MinecraftByteBuf buf) {
        this.entityId = buf.readVarInt();
        this.passengerIds = new int[buf.readVarInt()];
        for (int index = 0; index < this.passengerIds.length; index++) {
            this.passengerIds[index] = buf.readVarInt();
        }
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeVarInt(this.entityId);
        buf.writeVarInt(this.passengerIds.length);
        for (int entityId : this.passengerIds) {
            buf.writeVarInt(entityId);
        }
    }
}
