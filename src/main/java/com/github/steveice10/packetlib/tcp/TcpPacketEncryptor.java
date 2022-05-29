package com.github.steveice10.packetlib.tcp;

import com.github.steveice10.packetlib.crypt.PacketEncryption;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

import java.util.List;

public class TcpPacketEncryptor extends ByteToMessageCodec<ByteBuf> {
    private final PacketEncryption encryption;
    private byte[] decryptedArray = new byte[0];
    private byte[] encryptedArray = new byte[0];

    public TcpPacketEncryptor(PacketEncryption encryption) {
        this.encryption = encryption;
    }

    @Override
    public void encode(ChannelHandlerContext ctx, ByteBuf in, ByteBuf out) throws Exception {
        int length = in.readableBytes();
        byte[] bytes = this.getBytes(in);
        int outLength = this.encryption.getEncryptOutputSize(length);
        if( this.encryptedArray.length < outLength) {
            this.encryptedArray = new byte[outLength];
        }

        out.writeBytes(this.encryptedArray, 0, this.encryption.encrypt(bytes, 0, length, this.encryptedArray, 0));
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> out) throws Exception {
        int length = buf.readableBytes();
        byte[] bytes = this.getBytes(buf);
        ByteBuf result = ctx.alloc().heapBuffer(this.encryption.getDecryptOutputSize(length));
        result.writerIndex(this.encryption.decrypt(bytes, 0, length, result.array(), result.arrayOffset()));
        out.add(result);
    }

    private byte[] getBytes(ByteBuf buf) {
        int length = buf.readableBytes();
        if (this.decryptedArray.length < length) {
            this.decryptedArray = new byte[length];
        }

        buf.readBytes(this.decryptedArray, 0, length);
        return this.decryptedArray;
    }
}
