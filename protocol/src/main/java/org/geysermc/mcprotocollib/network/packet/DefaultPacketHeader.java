package org.geysermc.mcprotocollib.network.packet;

import io.netty.buffer.ByteBuf;
import org.geysermc.mcprotocollib.network.codec.PacketCodecHelper;

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
    public int readLength(ByteBuf buf, PacketCodecHelper codecHelper, int available) {
        return codecHelper.readVarInt(buf);
    }

    @Override
    public void writeLength(ByteBuf buf, PacketCodecHelper codecHelper, int length) {
        codecHelper.writeVarInt(buf, length);
    }

    @Override
    public int readPacketId(ByteBuf buf, PacketCodecHelper codecHelper) {
        return codecHelper.readVarInt(buf);
    }

    @Override
    public void writePacketId(ByteBuf buf, PacketCodecHelper codecHelper, int packetId) {
        codecHelper.writeVarInt(buf, packetId);
    }
}
