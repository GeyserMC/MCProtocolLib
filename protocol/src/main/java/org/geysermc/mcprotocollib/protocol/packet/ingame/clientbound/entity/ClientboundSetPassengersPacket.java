package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.entity;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ClientboundSetPassengersPacket implements MinecraftPacket {
    private final int entityId;
    private final int @NonNull [] passengerIds;

    public ClientboundSetPassengersPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.entityId = helper.readVarInt(in);
        this.passengerIds = new int[helper.readVarInt(in)];
        for (int index = 0; index < this.passengerIds.length; index++) {
            this.passengerIds[index] = helper.readVarInt(in);
        }
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writeVarInt(out, this.entityId);
        helper.writeVarInt(out, this.passengerIds.length);
        for (int entityId : this.passengerIds) {
            helper.writeVarInt(out, entityId);
        }
    }
}
