package org.geysermc.mcprotocollib.network.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.MessageToMessageCodec;
import org.geysermc.mcprotocollib.network.Session;
import org.geysermc.mcprotocollib.network.compression.PacketCompression;

import java.util.List;

public class TcpPacketCompression extends MessageToMessageCodec<ByteBuf, ByteBuf> {
    private static final int MAX_UNCOMPRESSED_SIZE = 8 * 1024 * 1024; // 8MiB

    private final Session session;
    private final PacketCompression compression;
    private final boolean validateDecompression;

    public TcpPacketCompression(Session session, PacketCompression compression, boolean validateDecompression) {
        this.session = session;
        this.compression = compression;
        this.validateDecompression = validateDecompression;
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        this.compression.close();
    }

    @Override
    public void encode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) {
        int uncompressed = msg.readableBytes();
        if (uncompressed > MAX_UNCOMPRESSED_SIZE) {
            throw new IllegalArgumentException("Packet too big (is " + uncompressed + ", should be less than " + MAX_UNCOMPRESSED_SIZE + ")");
        }

        ByteBuf outBuf = ctx.alloc().directBuffer(uncompressed);
        if (uncompressed < this.session.getCompressionThreshold()) {
            // Under the threshold, there is nothing to do.
            this.session.getCodecHelper().writeVarInt(outBuf, 0);
            outBuf.writeBytes(msg);
        } else {
            this.session.getCodecHelper().writeVarInt(outBuf, uncompressed);
            compression.deflate(msg, outBuf);
        }

        out.add(outBuf);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        int claimedUncompressedSize = this.session.getCodecHelper().readVarInt(in);
        if (claimedUncompressedSize == 0) {
            out.add(in.retain());
            return;
        }

        if (validateDecompression) {
            if (claimedUncompressedSize < this.session.getCompressionThreshold()) {
                throw new DecoderException("Badly compressed packet - size of " + claimedUncompressedSize + " is below server threshold of " + this.session.getCompressionThreshold());
            }

            if (claimedUncompressedSize > MAX_UNCOMPRESSED_SIZE) {
                throw new DecoderException("Badly compressed packet - size of " + claimedUncompressedSize + " is larger than protocol maximum of " + MAX_UNCOMPRESSED_SIZE);
            }
        }

        ByteBuf uncompressed = ctx.alloc().directBuffer(claimedUncompressedSize);
        try {
            compression.inflate(in, uncompressed, claimedUncompressedSize);
            out.add(uncompressed);
        } catch (Exception e) {
            uncompressed.release();
            throw new DecoderException("Failed to decompress packet", e);
        }
    }
}
