package com.github.steveice10.mc.protocol.packet.ingame.client.window;

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

import java.io.IOException;

@Data
@Setter(AccessLevel.NONE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ClientCreativeInventoryActionPacket implements Packet {
    private int slot;
    private @NonNull ItemStack clickedItem;

    @Override
    public void read(NetInput in) throws IOException {
        this.slot = in.readShort();
        this.clickedItem = ItemStack.read(in);
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeShort(this.slot);
        ItemStack.write(out, this.clickedItem);
    }

    @Override
    public boolean isPriority() {
        return false;
    }
}
