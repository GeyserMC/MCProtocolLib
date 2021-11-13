package com.github.steveice10.mc.protocol.packet.ingame.clientbound.inventory;

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.inventory.property.ContainerProperty;
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
public class ClientboundContainerSetDataPacket implements Packet {
    private final int containerId;
    private final int rawProperty;
    private final int value;

    public ClientboundContainerSetDataPacket(int containerId, ContainerProperty rawProperty, int value) {
        this(containerId, MagicValues.value(Integer.class, rawProperty), value);
    }

    public <T extends ContainerProperty> T getProperty(Class<T> type) {
        return MagicValues.key(type, this.value);
    }

    public ClientboundContainerSetDataPacket(NetInput in) throws IOException {
        this.containerId = in.readUnsignedByte();
        this.rawProperty = in.readShort();
        this.value = in.readShort();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeByte(this.containerId);
        out.writeShort(this.rawProperty);
        out.writeShort(this.value);
    }
}
