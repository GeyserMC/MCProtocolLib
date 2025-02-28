package org.geysermc.mcprotocollib.network.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import org.geysermc.mcprotocollib.network.NetworkConstants;
import org.geysermc.mcprotocollib.network.crypt.EncryptionConfig;

import java.util.List;

public class PacketEncryptorCodec extends ByteToMessageCodec<ByteBuf> {
    @Override
    public void encode(ChannelHandlerContext ctx, ByteBuf msg, ByteBuf out) throws Exception {
        EncryptionConfig config = ctx.channel().attr(NetworkConstants.ENCRYPTION_ATTRIBUTE_KEY).get();
        if (config == null) {
            out.writeBytes(msg);
            return;
        }

        ByteBuf heapBuf = this.ensureHeapBuffer(ctx.alloc(), msg);

        int inBytes = heapBuf.readableBytes();
        int baseOffset = heapBuf.arrayOffset() + heapBuf.readerIndex();

        try {
            config.encryption().encrypt(heapBuf.array(), baseOffset, inBytes, heapBuf.array(), baseOffset);
            out.writeBytes(heapBuf);
        } finally {
            heapBuf.release();
        }
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        EncryptionConfig config = ctx.channel().attr(NetworkConstants.ENCRYPTION_ATTRIBUTE_KEY).get();
        if (config == null) {
            out.add(in.readBytes(in.readableBytes()));
            return;
        }

        ByteBuf heapBuf = this.ensureHeapBuffer(ctx.alloc(), in).slice();

        int inBytes = heapBuf.readableBytes();
        int baseOffset = heapBuf.arrayOffset() + heapBuf.readerIndex();

        try {
            config.encryption().decrypt(heapBuf.array(), baseOffset, inBytes, heapBuf.array(), baseOffset);
            out.add(heapBuf);
        } catch (Exception e) {
            heapBuf.release();
            throw e;
        }
    }

    private ByteBuf ensureHeapBuffer(ByteBufAllocator alloc, ByteBuf buf) {
        if (buf.hasArray()) {
            return buf.retain();
        } else {
            ByteBuf heapBuf = alloc.heapBuffer(buf.readableBytes());
            heapBuf.writeBytes(buf);
            return heapBuf;
        }
    }
}
