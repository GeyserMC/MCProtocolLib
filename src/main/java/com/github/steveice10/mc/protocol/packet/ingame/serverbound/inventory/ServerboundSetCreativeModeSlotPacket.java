package com.github.steveice10.mc.protocol.packet.ingame.serverbound.inventory;

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
public class ServerboundSetCreativeModeSlotPacket implements Packet {
    private final int slot;
    private final @NonNull ItemStack clickedItem;

    public ServerboundSetCreativeModeSlotPacket(NetInput in) throws IOException {
        this.slot = in.readShort();
        this.clickedItem = ItemStack.read(in);
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeShort(this.slot);
        ItemStack.write(out, this.clickedItem);
    }
}
