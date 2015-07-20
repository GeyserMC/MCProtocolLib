package org.spacehq.packetlib.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import io.netty.handler.codec.CorruptedFrameException;
import org.spacehq.packetlib.Session;
import org.spacehq.packetlib.tcp.io.ByteBufNetInput;
import org.spacehq.packetlib.tcp.io.ByteBufNetOutput;

import java.util.List;

public class TcpPacketSizer extends ByteToMessageCodec<ByteBuf> {
    private Session session;

    public TcpPacketSizer(Session session) {
        this.session = session;
    }

    @Override
    public void encode(ChannelHandlerContext ctx, ByteBuf in, ByteBuf out) throws Exception {
        int length = in.readableBytes();
        out.ensureWritable(this.session.getPacketProtocol().getPacketHeader().getLengthSize(length) + length);
        this.session.getPacketProtocol().getPacketHeader().writeLength(new ByteBufNetOutput(out), length);
        out.writeBytes(in);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> out) throws Exception {
        int size = this.session.getPacketProtocol().getPacketHeader().getLengthSize();
        if(size > 0) {
            buf.markReaderIndex();
            byte[] lengthBytes = new byte[size];
            for(int index = 0; index < lengthBytes.length; index++) {
                if(!buf.isReadable()) {
                    buf.resetReaderIndex();
                    return;
                }

                lengthBytes[index] = buf.readByte();
                if((this.session.getPacketProtocol().getPacketHeader().isLengthVariable() && lengthBytes[index] >= 0) || index == size - 1) {
                    int length = this.session.getPacketProtocol().getPacketHeader().readLength(new ByteBufNetInput(Unpooled.wrappedBuffer(lengthBytes)), buf.readableBytes());
                    if(buf.readableBytes() < length) {
                        buf.resetReaderIndex();
                        return;
                    }

                    out.add(buf.readBytes(length));
                    return;
                }
            }

            throw new CorruptedFrameException("Length is too long.");
        } else {
            out.add(buf.readBytes(buf.readableBytes()));
        }
    }
}
