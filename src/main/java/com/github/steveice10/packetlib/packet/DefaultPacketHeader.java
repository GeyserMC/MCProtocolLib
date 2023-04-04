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

        if(isTheLengthRoundDownTo128(length)) {
            return 1;
        } else if(isTheLengthRoundDownTo16384(length)) {
            return 2;
        } else if(isTheLengthRoundDownTo2097152(length)) {
            return 3;
        } else if(isTheLengthRoundDownTo268435456(length)) {
            return 4;
        } else {
            return 5;
        }
    }

    private boolean isTheLengthRoundDownTo128(int length){
        return  (length&-128)==0;
    }
    private boolean isTheLengthRoundDownTo16384(int length){
        return  (length&-16384)==0;
    }
    private boolean isTheLengthRoundDownTo2097152(int length){
        return  (length&-2097152)==0;
    }

    private boolean isTheLengthRoundDownTo268435456(int length){
        return  (length&-268435456)==0;
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
