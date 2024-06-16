package org.geysermc.mcprotocollib.network.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import org.geysermc.mcprotocollib.network.crypt.PacketEncryption;

import java.util.List;

public class TcpPacketEncryptor extends MessageToMessageCodec<ByteBuf, ByteBuf> {
    private final PacketEncryption encryption;

    public TcpPacketEncryptor(PacketEncryption encryption) {
        this.encryption = encryption;
    }

    @Override
    public void encode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        ByteBuf heapBuf = this.ensureHeapBuffer(ctx.alloc(), msg);

        int inBytes = heapBuf.readableBytes();
        int baseOffset = heapBuf.arrayOffset() + heapBuf.readerIndex();

        try {
            encryption.encrypt(heapBuf.array(), baseOffset, inBytes, heapBuf.array(), baseOffset);
            out.add(heapBuf);
        } catch (Exception e) {
            heapBuf.release();
            throw new Exception("Error encrypting packet", e);
        }
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        ByteBuf heapBuf = this.ensureHeapBuffer(ctx.alloc(), in).slice();

        int inBytes = heapBuf.readableBytes();
        int baseOffset = heapBuf.arrayOffset() + heapBuf.readerIndex();

        try {
            encryption.decrypt(heapBuf.array(), baseOffset, inBytes, heapBuf.array(), baseOffset);
            out.add(heapBuf);
        } catch (Exception e) {
            heapBuf.release();
            throw new Exception("Error decrypting packet", e);
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
