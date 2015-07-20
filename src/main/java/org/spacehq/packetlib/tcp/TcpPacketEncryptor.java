package org.spacehq.packetlib.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import org.spacehq.packetlib.Session;

import java.util.List;

public class TcpPacketEncryptor extends ByteToMessageCodec<ByteBuf> {
    private Session session;
    private byte[] decryptedArray = new byte[0];
    private byte[] encryptedArray = new byte[0];

    public TcpPacketEncryptor(Session session) {
        this.session = session;
    }

    @Override
    public void encode(ChannelHandlerContext ctx, ByteBuf in, ByteBuf out) throws Exception {
        if(this.session.getPacketProtocol().getEncryption() != null) {
            int length = in.readableBytes();
            byte[] bytes = this.getBytes(in);
            int outLength = this.session.getPacketProtocol().getEncryption().getEncryptOutputSize(length);
            if(this.encryptedArray.length < outLength) {
                this.encryptedArray = new byte[outLength];
            }

            out.writeBytes(this.encryptedArray, 0, this.session.getPacketProtocol().getEncryption().encrypt(bytes, 0, length, this.encryptedArray, 0));
        } else {
            out.writeBytes(in);
        }
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> out) throws Exception {
        if(this.session.getPacketProtocol().getEncryption() != null) {
            int length = buf.readableBytes();
            byte[] bytes = this.getBytes(buf);
            ByteBuf result = ctx.alloc().heapBuffer(this.session.getPacketProtocol().getEncryption().getDecryptOutputSize(length));
            result.writerIndex(this.session.getPacketProtocol().getEncryption().decrypt(bytes, 0, length, result.array(), result.arrayOffset()));
            out.add(result);
        } else {
            out.add(buf.readBytes(buf.readableBytes()));
        }
    }

    private byte[] getBytes(ByteBuf buf) {
        int length = buf.readableBytes();
        if(this.decryptedArray.length < length) {
            this.decryptedArray = new byte[length];
        }

        buf.readBytes(this.decryptedArray, 0, length);
        return this.decryptedArray;
    }
}
