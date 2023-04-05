package com.github.steveice10.packetlib.packet;

import com.github.steveice10.packetlib.codec.PacketCodecHelper;
import io.netty.buffer.ByteBuf;

import java.io.IOException;

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

            int bits128 = 128;
            int bits16384 = 16384;
            int bits2097152 = 2097152;
            int bits268435456 = 268435456;

        if(isTheLengthRoundDownToBits(length, bits128)) {
            return 1;
        } else if(isTheLengthRoundDownToBits(length,bits16384)) {
            return 2;
        } else if(isTheLengthRoundDownToBits(length,bits2097152)) {
            return 3;
        } else if(isTheLengthRoundDownToBits(length,bits268435456)) {
            return 4;
        } else {
            return 5;
        }
    }

    private boolean isTheLengthRoundDownToBits(int length,int bits){
        return  (length&-bits)==0;
    }

    @Override
    public int readLength(ByteBuf buf, PacketCodecHelper codecHelper, int available) throws IOException {
        return codecHelper.readVarInt(buf);
    }

    @Override
    public void writeLength(ByteBuf buf, PacketCodecHelper codecHelper, int length) throws IOException {
        codecHelper.writeVarInt(buf, length);
    }

    @Override
    public int readPacketId(ByteBuf buf, PacketCodecHelper codecHelper) throws IOException {
        return codecHelper.readVarInt(buf);
    }

    @Override
    public void writePacketId(ByteBuf buf, PacketCodecHelper codecHelper, int packetId) throws IOException {
        codecHelper.writeVarInt(buf, packetId);
    }
}
