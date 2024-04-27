package org.geysermc.mcprotocollib.protocol.data.game.entity.metadata;

import io.netty.buffer.ByteBuf;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.data.game.entity.metadata.type.FloatEntityMetadata;

public class FloatMetadataType extends MetadataType<Float> {
    private final FloatReader primitiveReader;
    private final FloatWriter primitiveWriter;
    private final FloatEntityMetadataFactory primitiveFactory;

    protected FloatMetadataType(FloatReader reader, FloatWriter writer, FloatEntityMetadataFactory metadataFactory) {
        super(reader, writer, metadataFactory);

        this.primitiveReader = reader;
        this.primitiveWriter = writer;
        this.primitiveFactory = metadataFactory;
    }

    @Override
    public EntityMetadata<Float, FloatMetadataType> readMetadata(MinecraftCodecHelper helper, ByteBuf input, int id) {
        return this.primitiveFactory.createPrimitive(id, this, this.primitiveReader.readPrimitive(input));
    }

    public void writeMetadataPrimitive(ByteBuf output, float value) {
        this.primitiveWriter.writePrimitive(output, value);
    }

    @FunctionalInterface
    public interface FloatReader extends BasicReader<Float> {
        float readPrimitive(ByteBuf input);

        @Deprecated
        @Override
        default Float read(ByteBuf input) {
            return this.readPrimitive(input);
        }
    }

    @FunctionalInterface
    public interface FloatWriter extends BasicWriter<Float> {
        void writePrimitive(ByteBuf output, float value);

        @Deprecated
        @Override
        default void write(ByteBuf output, Float value) {
            this.writePrimitive(output, value);
        }
    }

    @FunctionalInterface
    public interface FloatEntityMetadataFactory extends EntityMetadataFactory<Float> {
        FloatEntityMetadata createPrimitive(int id, FloatMetadataType type, float value);

        @Deprecated
        @Override
        default EntityMetadata<Float, FloatMetadataType> create(int id, MetadataType<Float> type, Float value) {
            throw new UnsupportedOperationException("Unsupported read method! Use primitive createPrimitive!");
        }
    }
}
