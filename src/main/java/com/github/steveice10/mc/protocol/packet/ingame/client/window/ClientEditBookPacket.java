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
public class ClientEditBookPacket implements Packet {
    private @NonNull ItemStack book;
    private boolean signing;

    @Override
    public void read(NetInput in) throws IOException {
        this.book = ItemStack.read(in);
        this.signing = in.readBoolean();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        ItemStack.write(out, this.book);
        out.writeBoolean(this.signing);
    }

    @Override
    public boolean isPriority() {
        return false;
    }
}
