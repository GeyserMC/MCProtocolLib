package org.geysermc.mcprotocollib.protocol.data.game.entity.metadata;

import io.netty.buffer.ByteBuf;
import org.geysermc.mcprotocollib.protocol.data.game.entity.metadata.type.IntEntityMetadata;

public class IntMetadataType extends MetadataType<Integer> {
    private final IntReader primitiveReader;
    private final IntWriter primitiveWriter;
    private final IntEntityMetadataFactory primitiveFactory;

    protected IntMetadataType(IntReader reader, IntWriter writer, IntEntityMetadataFactory metadataFactory) {
        super(reader, writer, metadataFactory);

        this.primitiveReader = reader;
        this.primitiveWriter = writer;
        this.primitiveFactory = metadataFactory;
    }

    @Override
    public EntityMetadata<Integer, IntMetadataType> readMetadata(ByteBuf input, int id) {
        return this.primitiveFactory.createPrimitive(id, this, this.primitiveReader.readPrimitive(input));
    }

    public void writeMetadataPrimitive(ByteBuf output, int value) {
        this.primitiveWriter.writePrimitive(output, value);
    }

    @FunctionalInterface
    public interface IntReader extends Reader<Integer> {
        int readPrimitive(ByteBuf input);

        @Deprecated
        @Override
        default Integer read(ByteBuf input) {
            return this.readPrimitive(input);
        }
    }

    @FunctionalInterface
    public interface IntWriter extends Writer<Integer> {
        void writePrimitive(ByteBuf output, int value);

        @Deprecated
        @Override
        default void write(ByteBuf output, Integer value) {
            this.writePrimitive(output, value);
        }
    }

    @FunctionalInterface
    public interface IntEntityMetadataFactory extends EntityMetadataFactory<Integer> {
        IntEntityMetadata createPrimitive(int id, IntMetadataType type, int value);

        @Deprecated
        @Override
        default EntityMetadata<Integer, IntMetadataType> create(int id, MetadataType<Integer> type, Integer value) {
            throw new UnsupportedOperationException("Unsupported read method! Use primitive createPrimitive!");
        }
    }
}
