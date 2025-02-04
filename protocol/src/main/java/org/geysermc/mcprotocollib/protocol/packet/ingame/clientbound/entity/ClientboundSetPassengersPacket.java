package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.entity;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;

@Data
@With
@AllArgsConstructor
public class ClientboundSetPassengersPacket implements MinecraftPacket {
    private final int entityId;
    private final int @NonNull [] passengerIds;

    public ClientboundSetPassengersPacket(ByteBuf in) {
        this.entityId = MinecraftTypes.readVarInt(in);
        this.passengerIds = new int[MinecraftTypes.readVarInt(in)];
        for (int index = 0; index < this.passengerIds.length; index++) {
            this.passengerIds[index] = MinecraftTypes.readVarInt(in);
        }
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeVarInt(out, this.entityId);
        MinecraftTypes.writeVarInt(out, this.passengerIds.length);
        for (int entityId : this.passengerIds) {
            MinecraftTypes.writeVarInt(out, entityId);
        }
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
