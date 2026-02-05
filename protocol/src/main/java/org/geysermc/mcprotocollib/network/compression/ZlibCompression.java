package org.geysermc.mcprotocollib.network.compression;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.DecoderException;

import java.nio.ByteBuffer;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class ZlibCompression implements PacketCompression {
    private static final int ZLIB_BUFFER_SIZE = 8192;
    private final Deflater deflater;
    private final Inflater inflater;

    public ZlibCompression() {
        this(Deflater.DEFAULT_COMPRESSION);
    }

    public ZlibCompression(int level) {
        this.deflater = new Deflater(level);
        this.inflater = new Inflater();
    }

    @Override
    public void inflate(ByteBuf source, ByteBuf destination, int uncompressedSize) throws DataFormatException {
        ByteBuffer input;
        if (source.nioBufferCount() > 0) {
            input = source.nioBuffer();
            source.skipBytes(source.readableBytes());
        } else {
            input = ByteBuffer.allocateDirect(source.readableBytes());
            source.readBytes(input);
            input.flip();
        }
        inflater.setInput(input);

        try {
            ByteBuffer nioBuffer = destination.internalNioBuffer(0, uncompressedSize);
            int pos = nioBuffer.position();
            inflater.inflate(nioBuffer);
            int actualUncompressedSize = nioBuffer.position() - pos;
            if (actualUncompressedSize != uncompressedSize) {
                throw new DecoderException(
                    "Badly compressed packet - actual size of uncompressed payload "
                    + actualUncompressedSize
                    + " does not match declared size "
                    + uncompressedSize);
            } else {
                destination.writerIndex(destination.writerIndex() + actualUncompressedSize);
            }
        } finally {
            inflater.reset();
        }
    }

    @Override
    public void deflate(ByteBuf source, ByteBuf destination) {
        final int originalIndex = source.readerIndex();
        deflater.setInput(source.nioBuffer());
        deflater.finish();

        while (!deflater.finished()) {
            if (!destination.isWritable()) {
                destination.ensureWritable(ZLIB_BUFFER_SIZE);
            }

            ByteBuffer destNioBuf = destination.nioBuffer(destination.writerIndex(),
                destination.writableBytes());
            int produced = deflater.deflate(destNioBuf);
            destination.writerIndex(destination.writerIndex() + produced);
        }

        source.readerIndex(originalIndex + deflater.getTotalIn());
        deflater.reset();
    }

    @Override
    public void close() {
        deflater.end();
        inflater.end();
    }
}
