package org.geysermc.mcprotocollib.network.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.CorruptedFrameException;
import io.netty.handler.codec.MessageToMessageCodec;
import lombok.RequiredArgsConstructor;
import org.geysermc.mcprotocollib.network.codec.PacketCodecHelper;
import org.geysermc.mcprotocollib.network.packet.PacketHeader;

import java.util.List;

@RequiredArgsConstructor
public class TcpPacketSizer extends MessageToMessageCodec<ByteBuf, ByteBuf> {
    private final PacketHeader header;
    private final PacketCodecHelper codecHelper;

    @Override
    public void encode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        int size = header.getLengthSize();
        if (size == 0) {
            out.add(in.retain());
            return;
        }

        int length = in.readableBytes();
        int targetLength = header.getLengthSize(length) + length;
        ByteBuf resultBuf = ctx.alloc().buffer(targetLength);

        header.writeLength(resultBuf, codecHelper, length);
        resultBuf.writeBytes(in);

        out.add(resultBuf);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> out) {
        int size = header.getLengthSize();
        if (size == 0) {
            out.add(buf.retain());
            return;
        }

        buf.markReaderIndex();
        byte[] lengthBytes = new byte[size];
        for (int index = 0; index < lengthBytes.length; index++) {
            if (!buf.isReadable()) {
                buf.resetReaderIndex();
                return;
            }

            lengthBytes[index] = buf.readByte();
            if ((header.isLengthVariable() && lengthBytes[index] >= 0) || index == size - 1) {
                int length = header.readLength(Unpooled.wrappedBuffer(lengthBytes), codecHelper, buf.readableBytes());
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
