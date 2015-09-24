package org.spacehq.mc.protocol.packet;

import io.netty.buffer.ByteBuf;
import org.spacehq.mc.protocol.data.game.EntityMetadata;
import org.spacehq.mc.protocol.data.game.ItemStack;
import org.spacehq.mc.protocol.data.game.Position;
import org.spacehq.mc.protocol.util.NetUtil;
import org.spacehq.opennbt.tag.builtin.CompoundTag;
import org.spacehq.packetlib.tcp.io.ByteBufNetOutput;

import java.io.IOException;

public class PacketBufferOutput extends ByteBufNetOutput {

    public PacketBufferOutput(ByteBuf buffer) {
        super(buffer);
    }

    public void writeNBT(CompoundTag tag) throws IOException {
        NetUtil.writeNBT(this, tag);
    }

    public void writePosition(Position position) throws IOException {
        NetUtil.writePosition(this, position);
    }

    public void writeItem(ItemStack item) throws IOException {
        NetUtil.writeItem(this, item);
    }

    public void writeEntityMetadata(EntityMetadata[] metadata) throws IOException {
        NetUtil.writeEntityMetadata(this, metadata);
    }

}
