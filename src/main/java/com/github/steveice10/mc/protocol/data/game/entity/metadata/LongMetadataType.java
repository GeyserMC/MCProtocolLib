package com.github.steveice10.mc.protocol.data.game.entity.metadata;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.type.LongEntityMetadata;
import io.netty.buffer.ByteBuf;

import java.io.IOException;

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
    public EntityMetadata<Long, LongMetadataType> readMetadata(MinecraftCodecHelper helper, ByteBuf input, int id) throws IOException {
        return this.primitiveFactory.createPrimitive(id, this, this.primitiveReader.readPrimitive(helper, input));
    }

    public void writeMetadataPrimitive(MinecraftCodecHelper helper, ByteBuf output, long value) throws IOException {
        this.primitiveWriter.writePrimitive(helper, output, value);
    }

    @FunctionalInterface
    public interface LongReader extends HelperReader<Long> {
        long readPrimitive(MinecraftCodecHelper helper, ByteBuf input) throws IOException;

        @Deprecated
        @Override
        default Long read(MinecraftCodecHelper helper, ByteBuf input) throws IOException {
            return this.readPrimitive(helper, input);
        }
    }

    @FunctionalInterface
    public interface LongWriter extends HelperWriter<Long> {
        void writePrimitive(MinecraftCodecHelper helper, ByteBuf output, long value) throws IOException;

        @Deprecated
        @Override
        default void write(MinecraftCodecHelper helper, ByteBuf output, Long value) throws IOException {
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
