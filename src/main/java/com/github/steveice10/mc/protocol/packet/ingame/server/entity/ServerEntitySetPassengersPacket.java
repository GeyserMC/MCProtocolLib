package com.github.steveice10.mc.protocol.packet.ingame.server.entity;

import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.With;

import java.io.IOException;

@Data
@With
@Setter(AccessLevel.NONE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ServerEntitySetPassengersPacket implements Packet {
    private int entityId;
    private @NonNull int[] passengerIds;

    @Override
    public void read(NetInput in) throws IOException {
        this.entityId = in.readVarInt();
        this.passengerIds = new int[in.readVarInt()];
        for (int index = 0; index < this.passengerIds.length; index++) {
            this.passengerIds[index] = in.readVarInt();
        }
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(this.entityId);
        out.writeVarInt(this.passengerIds.length);
        for (int entityId : this.passengerIds) {
            out.writeVarInt(entityId);
        }
    }

    @Override
    public boolean isPriority() {
        return false;
    }
}
