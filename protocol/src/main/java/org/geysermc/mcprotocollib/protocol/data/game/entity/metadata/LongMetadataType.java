package org.geysermc.mcprotocollib.protocol.data.game.entity.metadata;

import io.netty.buffer.ByteBuf;
import org.geysermc.mcprotocollib.protocol.data.game.entity.metadata.type.LongEntityMetadata;

public class LongMetadataType extends MetadataType<Long> {
    private final LongReader primitiveReader;
    private final LongWriter primitiveWriter;
    private final LongEntityMetadataFactory primitiveFactory;

    protected LongMetadataType(LongReader reader, LongWriter writer, LongEntityMetadataFactory metadataFactory) {
        super(reader, writer, metadataFactory);

        this.primitiveReader = reader;
        this.primitiveWriter = writer;
        this.primitiveFactory = metadataFactory;
    }

    @Override
    public EntityMetadata<Long, LongMetadataType> readMetadata(ByteBuf input, int id) {
        return this.primitiveFactory.createPrimitive(id, this, this.primitiveReader.readPrimitive(input));
    }

    public void writeMetadataPrimitive(ByteBuf output, long value) {
        this.primitiveWriter.writePrimitive(output, value);
    }

    @FunctionalInterface
    public interface LongReader extends Reader<Long> {
        long readPrimitive(ByteBuf input);

        @Deprecated
        @Override
        default Long read(ByteBuf input) {
            return this.readPrimitive(input);
        }
    }

    @FunctionalInterface
    public interface LongWriter extends Writer<Long> {
        void writePrimitive(ByteBuf output, long value);

        @Deprecated
        @Override
        default void write(ByteBuf output, Long value) {
            this.writePrimitive(output, value);
        }
    }

    @FunctionalInterface
    public interface LongEntityMetadataFactory extends EntityMetadataFactory<Long> {
        LongEntityMetadata createPrimitive(int id, LongMetadataType type, long value);

        @Deprecated
        @Override
        default EntityMetadata<Long, LongMetadataType> create(int id, MetadataType<Long> type, Long value) {
            throw new UnsupportedOperationException("Unsupported read method! Use primitive createPrimitive!");
        }
    }
}
