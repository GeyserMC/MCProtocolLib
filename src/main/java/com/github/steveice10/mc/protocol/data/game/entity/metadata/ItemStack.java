package com.github.steveice10.mc.protocol.data.game.entity.metadata;

import com.github.steveice10.mc.protocol.data.game.NBT;
import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.IOException;

@Data
@AllArgsConstructor
public class ItemStack {
    private final int id;
    private final int amount;
    private final CompoundTag nbt;

    public ItemStack(int id) {
        this(id, 1);
    }

    public ItemStack(int id, int amount) {
        this(id, amount, null);
    }

    public static ItemStack read(NetInput in) throws IOException {
        boolean present = in.readBoolean();
        if (!present) {
            return null;
        }

        int item = in.readVarInt();
        return new ItemStack(item, in.readByte(), NBT.read(in));
    }

    public static void write(NetOutput out, ItemStack item) throws IOException {
        out.writeBoolean(item != null);
        if (item != null) {
            out.writeVarInt(item.getId());
            out.writeByte(item.getAmount());
            NBT.write(out, item.getNbt());
        }
    }
}
