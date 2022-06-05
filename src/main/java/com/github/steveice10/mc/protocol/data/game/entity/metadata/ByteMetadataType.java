package com.github.steveice10.mc.protocol.data.game.entity.metadata;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.type.ByteEntityMetadata;
import io.netty.buffer.ByteBuf;

import java.io.IOException;

public class ByteMetadataType extends MetadataType<Byte> {
    private final ByteReader primitiveReader;
    private final ByteWriter primitiveWriter;
    private final ByteEntityMetadataFactory primitiveFactory;
    
    protected ByteMetadataType(ByteReader reader, ByteWriter writer, ByteEntityMetadataFactory metadataFactory) {
        super(reader, writer, metadataFactory);

        this.primitiveReader = reader;
        this.primitiveWriter = writer;
        this.primitiveFactory = metadataFactory;
    }

    @Override
    public EntityMetadata<Byte, ByteMetadataType> readMetadata(MinecraftCodecHelper helper, ByteBuf input, int id) throws IOException {
        return this.primitiveFactory.createPrimitive(id, this, this.primitiveReader.readPrimitive(input));
    }

    public void writeMetadataPrimitive(ByteBuf output, byte value) throws IOException {
        this.primitiveWriter.writePrimitive(output, value);
    }

    @FunctionalInterface
    public interface ByteReader extends BasicReader<Byte> {
        byte readPrimitive(ByteBuf input) throws IOException;

        @Deprecated
        @Override
        default Byte read(ByteBuf input) throws IOException {
            return this.readPrimitive(input);
        }
    }

    @FunctionalInterface
    public interface ByteWriter extends BasicWriter<Byte> {
        void writePrimitive(ByteBuf output, byte value) throws IOException;

        @Deprecated
        @Override
        default void write(ByteBuf output, Byte value) throws IOException {
            this.writePrimitive(output, value);
        }
    }

    @FunctionalInterface
    public interface ByteEntityMetadataFactory extends EntityMetadataFactory<Byte> {
        ByteEntityMetadata createPrimitive(int id, ByteMetadataType type, byte value);

        @Deprecated
        @Override
        default EntityMetadata<Byte, ByteMetadataType> create(int id, MetadataType<Byte> type, Byte value) {
            throw new UnsupportedOperationException("Unsupported read method! Use primitive createPrimitive!");
        }
    }
}
