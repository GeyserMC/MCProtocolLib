package com.github.steveice10.mc.protocol.packet.ingame.serverbound.inventory;

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
public class ServerboundContainerButtonClickPacket implements Packet {
    private final int containerId;
    private final int buttonId;

    public ServerboundContainerButtonClickPacket(NetInput in) throws IOException {
        this.containerId = in.readByte();
        this.buttonId = in.readByte();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeByte(this.containerId);
        out.writeByte(this.buttonId);
    }
}
