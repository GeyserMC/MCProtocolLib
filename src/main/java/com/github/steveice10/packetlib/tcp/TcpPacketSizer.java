package com.github.steveice10.packetlib.tcp;

import com.github.steveice10.packetlib.Session;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import io.netty.handler.codec.CorruptedFrameException;

import java.util.List;

public class TcpPacketSizer extends ByteToMessageCodec<ByteBuf> {
    private final Session session;
    private final int size;

    public TcpPacketSizer(Session session, int size) {
        this.session = session;
        this.size = size;
    }

    @Override
    public void encode(ChannelHandlerContext ctx, ByteBuf in, ByteBuf out) throws Exception {
        int length = in.readableBytes();
        out.ensureWritable(this.session.getPacketProtocol().getPacketHeader().getLengthSize(length) + length);
        this.session.getPacketProtocol().getPacketHeader().writeLength(out, this.session.getCodecHelper(), length);
        out.writeBytes(in);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> out) throws Exception {
        buf.markReaderIndex();
        byte[] lengthBytes = new byte[size];
        for (int index = 0; index < lengthBytes.length; index++) {
            if (!buf.isReadable()) {
                buf.resetReaderIndex();
                return;
            }

            lengthBytes[index] = buf.readByte();
            if ((this.session.getPacketProtocol().getPacketHeader().isLengthVariable() && lengthBytes[index] >= 0) || index == size - 1) {
                int length = this.session.getPacketProtocol().getPacketHeader().readLength(Unpooled.wrappedBuffer(lengthBytes), this.session.getCodecHelper(), buf.readableBytes());
                if (buf.readableBytes() < length) {
                    buf.resetReaderIndex();
                    return;
                }

                out.add(buf.readBytes(length));
                return;
            }
        }

        throw new CorruptedFrameException("Length is too long.");
    }
}
