package org.geysermc.mcprotocollib.network.packet;

import io.netty.buffer.ByteBuf;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;

/**
 * The default packet header, using a varint packet length and id.
 */
public class DefaultPacketHeader implements PacketHeader {
    @Override
    public boolean isLengthVariable() {
        return true;
    }

    @Override
    public int getLengthSize() {
        return 5;
    }

    @Override
    public int getLengthSize(int length) {
        if ((length & -128) == 0) {
            return 1;
        } else if ((length & -16384) == 0) {
            return 2;
        } else if ((length & -2097152) == 0) {
            return 3;
        } else if ((length & -268435456) == 0) {
            return 4;
        } else {
            return 5;
        }
    }

    @Override
    public int readLength(ByteBuf buf, int available) {
        return MinecraftTypes.readVarInt(buf);
    }

    @Override
    public void writeLength(ByteBuf buf, int length) {
        MinecraftTypes.writeVarInt(buf, length);
    }

    @Override
    public int readPacketId(ByteBuf buf) {
        return MinecraftTypes.readVarInt(buf);
    }

    @Override
    public void writePacketId(ByteBuf buf, int packetId) {
        MinecraftTypes.writeVarInt(buf, packetId);
    }
}
