package org.geysermc.mcprotocollib.protocol.data.game.entity.metadata;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import org.geysermc.mcprotocollib.protocol.data.game.entity.metadata.type.ByteEntityMetadata;

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
    public EntityMetadata<Byte, ByteMetadataType> readMetadata(MinecraftByteBuf input, int id) {
        return this.primitiveFactory.createPrimitive(id, this, this.primitiveReader.readPrimitive(input));
    }

    public void writeMetadataPrimitive(MinecraftByteBuf output, byte value) {
        this.primitiveWriter.writePrimitive(output, value);
    }

    @FunctionalInterface
    public interface ByteReader extends Reader<Byte> {
        byte readPrimitive(MinecraftByteBuf input);

        @Deprecated
        @Override
        default Byte read(MinecraftByteBuf input) {
            return this.readPrimitive(input);
        }
    }

    @FunctionalInterface
    public interface ByteWriter extends Writer<Byte> {
        void writePrimitive(MinecraftByteBuf output, byte value);

        @Deprecated
        @Override
        default void write(MinecraftByteBuf output, Byte value) {
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
