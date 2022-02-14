package com.github.steveice10.mc.protocol.packet.ingame.clientbound.entity;

import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;

import javax.annotation.Nonnull;
import java.io.IOException;

@Data
@With
@AllArgsConstructor
public class ClientboundRemoveEntitiesPacket implements Packet {
    private final @Nonnull int[] entityIds;

    public ClientboundRemoveEntitiesPacket(NetInput in) throws IOException {
        this.entityIds = new int[in.readVarInt()];
        for (int i = 0; i < this.entityIds.length; i++) {
            this.entityIds[i] = in.readVarInt();
        }
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(this.entityIds.length);
        for (int entityId : this.entityIds) {
            out.writeVarInt(entityId);
        }
    }
}
