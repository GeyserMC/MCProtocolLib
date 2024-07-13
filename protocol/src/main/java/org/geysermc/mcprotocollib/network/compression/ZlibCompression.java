package org.geysermc.mcprotocollib.network.compression;

import io.netty.buffer.ByteBuf;

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
        final int originalIndex = source.readerIndex();
        inflater.setInput(source.nioBuffer());

        try {
            while (!inflater.finished() && inflater.getBytesWritten() < uncompressedSize) {
                if (!destination.isWritable()) {
                    destination.ensureWritable(ZLIB_BUFFER_SIZE);
                }

                ByteBuffer destNioBuf = destination.nioBuffer(destination.writerIndex(),
                    destination.writableBytes());
                int produced = inflater.inflate(destNioBuf);
                destination.writerIndex(destination.writerIndex() + produced);
            }

            if (!inflater.finished()) {
                throw new DataFormatException("Received a deflate stream that was too large, wanted " + uncompressedSize);
            }

            source.readerIndex(originalIndex + inflater.getTotalIn());
        } finally {
            inflater.reset();
        }
    }

    @Override
    public void deflate(ByteBuf source, ByteBuf destination) {
        final int origIdx = source.readerIndex();
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

        source.readerIndex(origIdx + deflater.getTotalIn());
        deflater.reset();
    }

    @Override
    public void close() {
        deflater.end();
        inflater.end();
    }
}
