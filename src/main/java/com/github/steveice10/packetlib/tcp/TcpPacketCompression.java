package com.github.steveice10.packetlib.tcp;

import com.github.steveice10.packetlib.Session;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import io.netty.handler.codec.DecoderException;

import java.util.List;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class TcpPacketCompression extends ByteToMessageCodec<ByteBuf> {
    private static final int MAX_UNCOMPRESSED_SIZE = 8388608;

    private final Session session;
    private final Deflater deflater = new Deflater();
    private final Inflater inflater = new Inflater();
    private final byte[] buf = new byte[8192];
    private final boolean validateDecompression;

    public TcpPacketCompression(Session session, boolean validateDecompression) {
        this.session = session;
        this.validateDecompression = validateDecompression;
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        super.handlerRemoved(ctx);

        this.deflater.end();
        this.inflater.end();
    }

    @Override
    public void encode(ChannelHandlerContext ctx, ByteBuf in, ByteBuf out) throws Exception {
        int readable = in.readableBytes();
        if(readable < this.session.getCompressionThreshold()) {
            this.session.getCodecHelper().writeVarInt(out, 0);
            out.writeBytes(in);
        } else {
            byte[] bytes = new byte[readable];
            in.readBytes(bytes);
            this.session.getCodecHelper().writeVarInt(out, bytes.length);
            this.deflater.setInput(bytes, 0, readable);
            this.deflater.finish();
            while(!this.deflater.finished()) {
                int length = this.deflater.deflate(this.buf);
                out.writeBytes(this.buf, 0, length);
            }

            this.deflater.reset();
        }
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> out) throws Exception {
        if (buf.readableBytes() != 0) {
            int size = this.session.getCodecHelper().readVarInt(buf);
            if (size == 0) {
                out.add(buf.readBytes(buf.readableBytes()));
            } else {
                if (validateDecompression) { // This is sectioned off as of at least Java Edition 1.18
                    if (size < this.session.getCompressionThreshold()) {
                        throw new DecoderException("Badly compressed packet: size of " + size + " is below threshold of " + this.session.getCompressionThreshold() + ".");
                    }

                    if (size > MAX_UNCOMPRESSED_SIZE) {
                        throw new DecoderException("Badly compressed packet: size of " + size + " is larger than protocol maximum of " + MAX_UNCOMPRESSED_SIZE + ".");
                    }
                }

                byte[] bytes = new byte[buf.readableBytes()];
                buf.readBytes(bytes);
                this.inflater.setInput(bytes);
                byte[] inflated = new byte[size];
                this.inflater.inflate(inflated);
                out.add(Unpooled.wrappedBuffer(inflated));
                this.inflater.reset();
            }
        }
    }
}
