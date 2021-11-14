package com.github.steveice10.mc.protocol.packet.ingame.clientbound.inventory;

import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;

import java.io.IOException;

@Data
@With
@AllArgsConstructor
public class ClientboundHorseScreenOpenPacket implements Packet {
    private final int containerId;
    private final int numberOfSlots;
    private final int entityId;

    public ClientboundHorseScreenOpenPacket(NetInput in) throws IOException {
        this.containerId = in.readByte();
        this.numberOfSlots = in.readVarInt();
        this.entityId = in.readInt();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeByte(this.containerId);
        out.writeVarInt(this.numberOfSlots);
        out.writeInt(this.entityId);
    }
}
