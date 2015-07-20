package org.spacehq.packetlib.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import io.netty.handler.codec.DecoderException;
import org.spacehq.packetlib.Session;
import org.spacehq.packetlib.tcp.io.ByteBufNetInput;
import org.spacehq.packetlib.tcp.io.ByteBufNetOutput;

import java.util.List;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class TcpPacketCompression extends ByteToMessageCodec<ByteBuf> {
    private static final int MAX_COMPRESSED_SIZE = 2097152;

    private Session session;
    private Deflater deflater = new Deflater();
    private Inflater inflater = new Inflater();
    private byte buf[] = new byte[8192];

    public TcpPacketCompression(Session session) {
        this.session = session;
    }

    @Override
    public void encode(ChannelHandlerContext ctx, ByteBuf in, ByteBuf out) throws Exception {
        int readable = in.readableBytes();
        ByteBufNetOutput output = new ByteBufNetOutput(out);
        if(readable < this.session.getCompressionThreshold()) {
            output.writeVarInt(0);
            out.writeBytes(in);
        } else {
            byte[] bytes = new byte[readable];
            in.readBytes(bytes);
            output.writeVarInt(bytes.length);
            this.deflater.setInput(bytes, 0, readable);
            this.deflater.finish();
            while(!this.deflater.finished()) {
                int length = this.deflater.deflate(this.buf);
                output.writeBytes(this.buf, length);
            }

            this.deflater.reset();
        }
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> out) throws Exception {
        if(buf.readableBytes() != 0) {
            ByteBufNetInput in = new ByteBufNetInput(buf);
            int size = in.readVarInt();
            if(size == 0) {
                out.add(buf.readBytes(buf.readableBytes()));
            } else {
                if(size < this.session.getCompressionThreshold()) {
                    throw new DecoderException("Badly compressed packet: size of " + size + " is below threshold of " + this.session.getCompressionThreshold() + ".");
                }

                if(size > MAX_COMPRESSED_SIZE) {
                    throw new DecoderException("Badly compressed packet: size of " + size + " is larger than protocol maximum of " + MAX_COMPRESSED_SIZE + ".");
                }

                byte[] bytes = new byte[buf.readableBytes()];
                in.readBytes(bytes);
                this.inflater.setInput(bytes);
                byte[] inflated = new byte[size];
                this.inflater.inflate(inflated);
                out.add(Unpooled.wrappedBuffer(inflated));
                this.inflater.reset();
            }
        }
    }
}
