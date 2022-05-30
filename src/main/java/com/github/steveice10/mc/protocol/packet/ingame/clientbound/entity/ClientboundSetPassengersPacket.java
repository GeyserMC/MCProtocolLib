package com.github.steveice10.mc.protocol.packet.ingame.clientbound.entity;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;

import java.io.IOException;

@Data
@With
@AllArgsConstructor
public class ClientboundSetPassengersPacket implements MinecraftPacket {
    private final int entityId;
    private final @NonNull int[] passengerIds;

    public ClientboundSetPassengersPacket(ByteBuf in, MinecraftCodecHelper helper) throws IOException {
        this.entityId = helper.readVarInt(in);
        this.passengerIds = new int[helper.readVarInt(in)];
        for (int index = 0; index < this.passengerIds.length; index++) {
            this.passengerIds[index] = helper.readVarInt(in);
        }
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
        helper.writeVarInt(out, this.entityId);
        helper.writeVarInt(out, this.passengerIds.length);
        for (int entityId : this.passengerIds) {
            helper.writeVarInt(out, entityId);
        }
    }
}
