package com.github.steveice10.mc.protocol.packet.ingame.clientbound.inventory;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import com.github.steveice10.mc.protocol.data.game.inventory.property.ContainerProperty;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;

import java.io.IOException;

@Data
@With
@AllArgsConstructor
public class ClientboundContainerSetDataPacket implements MinecraftPacket {
    private final int containerId;
    private final int rawProperty;
    private final int value;

    public ClientboundContainerSetDataPacket(int containerId, ContainerProperty rawProperty, int value) {
        this(containerId, rawProperty.ordinal(), value);
    }

    public ClientboundContainerSetDataPacket(ByteBuf in, MinecraftCodecHelper helper) throws IOException {
        this.containerId = in.readUnsignedByte();
        this.rawProperty = in.readShort();
        this.value = in.readShort();
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
        out.writeByte(this.containerId);
        out.writeShort(this.rawProperty);
        out.writeShort(this.value);
    }
}
