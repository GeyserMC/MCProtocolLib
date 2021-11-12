package com.github.steveice10.mc.protocol.packet.ingame.clientbound.window;

import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.With;

import java.io.IOException;

@Data
@With
@AllArgsConstructor
public class ClientboundHorseScreenOpenPacket implements Packet {
    private final int windowId;
    private final int numberOfSlots;
    private final int entityId;

    public ClientboundHorseScreenOpenPacket(NetInput in) throws IOException {
        this.windowId = in.readByte();
        this.numberOfSlots = in.readVarInt();
        this.entityId = in.readInt();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeByte(this.windowId);
        out.writeVarInt(this.numberOfSlots);
        out.writeInt(this.entityId);
    }
}
