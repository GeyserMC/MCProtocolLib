package com.github.steveice10.mc.protocol.packet.ingame.clientbound.inventory;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;

import java.io.IOException;

@Data
@With
@AllArgsConstructor
public class ClientboundContainerSetContentPacket implements Packet {
    private final int containerId;
    private final int stateId;
    private final @NonNull ItemStack[] items;
    private final ItemStack carriedItem;

    public ClientboundContainerSetContentPacket(NetInput in) throws IOException {
        this.containerId = in.readUnsignedByte();
        this.stateId = in.readVarInt();
        this.items = new ItemStack[in.readVarInt()];
        for (int index = 0; index < this.items.length; index++) {
            this.items[index] = ItemStack.read(in);
        }
        this.carriedItem = ItemStack.read(in);
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeByte(this.containerId);
        out.writeVarInt(this.stateId);
        out.writeVarInt(this.items.length);
        for (ItemStack item : this.items) {
            ItemStack.write(out, item);
        }
        ItemStack.write(out, this.carriedItem);
    }
}
