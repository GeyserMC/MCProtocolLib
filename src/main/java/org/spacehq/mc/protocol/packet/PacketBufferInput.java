package org.spacehq.mc.protocol.packet;

import io.netty.buffer.ByteBuf;
import org.spacehq.mc.protocol.data.game.EntityMetadata;
import org.spacehq.mc.protocol.data.game.ItemStack;
import org.spacehq.mc.protocol.data.game.Position;
import org.spacehq.mc.protocol.util.NetUtil;
import org.spacehq.opennbt.tag.builtin.CompoundTag;
import org.spacehq.packetlib.tcp.io.ByteBufNetInput;

import java.io.IOException;

public class PacketBufferInput extends ByteBufNetInput {

    public PacketBufferInput(ByteBuf buf) {
        super(buf);
    }

    public CompoundTag readNBT() throws IOException {
        return NetUtil.readNBT(this);
    }

    public Position readPosition() throws IOException {
        return NetUtil.readPosition(this);
    }

    public ItemStack readItem() throws IOException {
        return NetUtil.readItem(this);
    }

    public EntityMetadata[] readEntityMetadata() throws IOException {
        return NetUtil.readEntityMetadata(this);
    }

}
