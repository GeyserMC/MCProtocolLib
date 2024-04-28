package org.geysermc.mcprotocollib.protocol.data.game.entity.metadata;

import io.netty.buffer.ByteBuf;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.data.game.entity.metadata.type.BooleanEntityMetadata;

public class BooleanMetadataType extends MetadataType<Boolean> {
    private final BooleanReader primitiveReader;
    private final BooleanWriter primitiveWriter;
    private final BooleanEntityMetadataFactory primitiveFactory;

    protected BooleanMetadataType(BooleanReader reader, BooleanWriter writer, BooleanEntityMetadataFactory metadataFactory) {
        super(reader, writer, metadataFactory);

        this.primitiveReader = reader;
        this.primitiveWriter = writer;
        this.primitiveFactory = metadataFactory;
    }

    @Override
    public EntityMetadata<Boolean, BooleanMetadataType> readMetadata(MinecraftCodecHelper helper, ByteBuf input, int id) {
        return this.primitiveFactory.createPrimitive(id, this, this.primitiveReader.readPrimitive(input));
    }

    public void writeMetadataPrimitive(ByteBuf output, boolean value) {
        this.primitiveWriter.writePrimitive(output, value);
    }

    @FunctionalInterface
    public interface BooleanReader extends BasicReader<Boolean> {
        boolean readPrimitive(ByteBuf input);

        @Deprecated
        @Override
        default Boolean read(ByteBuf input) {
            return this.readPrimitive(input);
        }
    }

    @FunctionalInterface
    public interface BooleanWriter extends BasicWriter<Boolean> {
        void writePrimitive(ByteBuf output, boolean value);

        @Deprecated
        @Override
        default void write(ByteBuf output, Boolean value) {
            this.writePrimitive(output, value);
        }
    }

    @FunctionalInterface
    public interface BooleanEntityMetadataFactory extends EntityMetadataFactory<Boolean> {
        BooleanEntityMetadata createPrimitive(int id, BooleanMetadataType type, boolean value);

        @Deprecated
        @Override
        default EntityMetadata<Boolean, BooleanMetadataType> create(int id, MetadataType<Boolean> type, Boolean value) {
            throw new UnsupportedOperationException("Unsupported read method! Use primitive createPrimitive!");
        }
    }
}
