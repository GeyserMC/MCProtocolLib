package org.geysermc.mcprotocollib.protocol.data.game.entity.metadata;

import io.netty.buffer.ByteBuf;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
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
    public EntityMetadata<Long, LongMetadataType> readMetadata(MinecraftCodecHelper helper, ByteBuf input, int id) {
        return this.primitiveFactory.createPrimitive(id, this, this.primitiveReader.readPrimitive(helper, input));
    }

    public void writeMetadataPrimitive(MinecraftCodecHelper helper, ByteBuf output, long value) {
        this.primitiveWriter.writePrimitive(helper, output, value);
    }

    @FunctionalInterface
    public interface LongReader extends Reader<Long> {
        long readPrimitive(MinecraftCodecHelper helper, ByteBuf input);

        @Deprecated
        @Override
        default Long read(MinecraftCodecHelper helper, ByteBuf input) {
            return this.readPrimitive(helper, input);
        }
    }

    @FunctionalInterface
    public interface LongWriter extends Writer<Long> {
        void writePrimitive(MinecraftCodecHelper helper, ByteBuf output, long value);

        @Deprecated
        @Override
        default void write(MinecraftCodecHelper helper, ByteBuf output, Long value) {
            this.writePrimitive(helper, output, value);
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
