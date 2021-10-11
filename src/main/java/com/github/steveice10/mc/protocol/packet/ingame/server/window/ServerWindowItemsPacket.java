package com.github.steveice10.mc.protocol.packet.ingame.server.window;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
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
public class ServerWindowItemsPacket implements Packet {
    private int windowId;
    private int stateId;
    private @NonNull ItemStack[] items;
    private ItemStack carriedItem;

    @Override
    public void read(NetInput in) throws IOException {
        this.windowId = in.readUnsignedByte();
        this.stateId = in.readVarInt();
        this.items = new ItemStack[in.readVarInt()];
        for (int index = 0; index < this.items.length; index++) {
            this.items[index] = ItemStack.read(in);
        }
        this.carriedItem = ItemStack.read(in);
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeByte(this.windowId);
        out.writeVarInt(this.stateId);
        out.writeVarInt(this.items.length);
        for (ItemStack item : this.items) {
            ItemStack.write(out, item);
        }
        ItemStack.write(out, this.carriedItem);
    }

    @Override
    public boolean isPriority() {
        return false;
    }
}
