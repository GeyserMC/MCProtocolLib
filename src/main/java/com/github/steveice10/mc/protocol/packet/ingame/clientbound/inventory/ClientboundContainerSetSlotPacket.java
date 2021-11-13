package com.github.steveice10.mc.protocol.packet.ingame.clientbound.inventory;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
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
public class ClientboundContainerSetSlotPacket implements Packet {
    private final int containerId;
    private final int stateId;
    private final int slot;
    private final ItemStack item;

    public ClientboundContainerSetSlotPacket(NetInput in) throws IOException {
        this.containerId = in.readUnsignedByte();
        this.stateId = in.readVarInt();
        this.slot = in.readShort();
        this.item = ItemStack.read(in);
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeByte(this.containerId);
        out.writeVarInt(this.stateId);
        out.writeShort(this.slot);
        ItemStack.write(out, this.item);
    }
}
