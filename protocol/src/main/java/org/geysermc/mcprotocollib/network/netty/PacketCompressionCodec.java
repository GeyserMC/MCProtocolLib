package org.geysermc.mcprotocollib.network.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import io.netty.handler.codec.DecoderException;
import lombok.RequiredArgsConstructor;
import org.geysermc.mcprotocollib.network.NetworkConstants;
import org.geysermc.mcprotocollib.network.compression.CompressionConfig;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;

import java.util.List;

@RequiredArgsConstructor
public class PacketCompressionCodec extends ByteToMessageCodec<ByteBuf> {
    private static final int MAX_UNCOMPRESSED_SIZE = 8 * 1024 * 1024; // 8MiB

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        CompressionConfig config = ctx.channel().attr(NetworkConstants.COMPRESSION_ATTRIBUTE_KEY).get();
        if (config == null) {
            return;
        }

        config.compression().close();
    }

    @Override
    public void encode(ChannelHandlerContext ctx, ByteBuf msg, ByteBuf out) {
        CompressionConfig config = ctx.channel().attr(NetworkConstants.COMPRESSION_ATTRIBUTE_KEY).get();
        if (config == null) {
            out.writeBytes(msg);
            return;
        }

        int uncompressed = msg.readableBytes();
        if (uncompressed > MAX_UNCOMPRESSED_SIZE) {
            throw new IllegalArgumentException("Packet too big (is " + uncompressed + ", should be less than " + MAX_UNCOMPRESSED_SIZE + ")");
        }

        ByteBuf outBuf = ctx.alloc().directBuffer(uncompressed);
        if (uncompressed < config.threshold()) {
            // Under the threshold, there is nothing to do.
            MinecraftTypes.writeVarInt(outBuf, 0);
            outBuf.writeBytes(msg);
        } else {
            MinecraftTypes.writeVarInt(outBuf, uncompressed);
            config.compression().deflate(msg, outBuf);
        }

        out.writeBytes(outBuf);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        CompressionConfig config = ctx.channel().attr(NetworkConstants.COMPRESSION_ATTRIBUTE_KEY).get();
        if (config == null) {
            out.add(in.retain());
            return;
        }

        int claimedUncompressedSize = MinecraftTypes.readVarInt(in);
        if (claimedUncompressedSize == 0) {
            out.add(in.retain());
            return;
        }

        if (config.validateDecompression()) {
            if (claimedUncompressedSize < config.threshold()) {
                throw new DecoderException("Badly compressed packet - size of " + claimedUncompressedSize + " is below server threshold of " + config.threshold());
            }

            if (claimedUncompressedSize > MAX_UNCOMPRESSED_SIZE) {
                throw new DecoderException("Badly compressed packet - size of " + claimedUncompressedSize + " is larger than protocol maximum of " + MAX_UNCOMPRESSED_SIZE);
            }
        }

        ByteBuf uncompressed = ctx.alloc().directBuffer(claimedUncompressedSize);
        try {
            config.compression().inflate(in, uncompressed, claimedUncompressedSize);
            out.add(uncompressed);
        } catch (Exception e) {
            uncompressed.release();
            throw new DecoderException("Failed to decompress packet", e);
        }
    }
}
