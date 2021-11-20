package com.github.steveice10.mc.protocol.data.game.entity.metadata;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.type.BooleanEntityMetadata;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;

import java.io.IOException;

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
    public EntityMetadata<Boolean, BooleanMetadataType> readMetadata(NetInput input, int id) throws IOException {
        return this.primitiveFactory.createPrimitive(id, this, this.primitiveReader.readPrimitive(input));
    }

    public void writeMetadataPrimitive(NetOutput output, boolean value) throws IOException {
       this.primitiveWriter.writePrimitive(output, value);
    }

    @FunctionalInterface
    public interface BooleanReader extends Reader<Boolean> {
        boolean readPrimitive(NetInput input) throws IOException;

        @Deprecated
        @Override
        default Boolean read(NetInput input) throws IOException {
            return this.readPrimitive(input);
        }
    }

    @FunctionalInterface
    public interface BooleanWriter extends Writer<Boolean> {
        void writePrimitive(NetOutput output, boolean value) throws IOException;

        @Deprecated
        @Override
        default void write(NetOutput output, Boolean value) throws IOException {
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
