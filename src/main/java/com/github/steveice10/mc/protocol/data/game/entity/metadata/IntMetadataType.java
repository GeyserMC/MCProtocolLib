package com.github.steveice10.mc.protocol.data.game.entity.metadata;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.type.IntEntityMetadata;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;

import java.io.IOException;

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
    public EntityMetadata<Integer, IntMetadataType> readMetadata(NetInput input, int id) throws IOException {
        return this.primitiveFactory.createPrimitive(id, this, this.primitiveReader.readPrimitive(input));
    }

    public void writeMetadataPrimitive(NetOutput output, int value) throws IOException {
        this.primitiveWriter.writePrimitive(output, value);
    }

    @FunctionalInterface
    public interface IntReader extends Reader<Integer> {
        int readPrimitive(NetInput input) throws IOException;

        @Deprecated
        @Override
        default Integer read(NetInput input) throws IOException {
            return this.readPrimitive(input);
        }
    }

    @FunctionalInterface
    public interface IntWriter extends Writer<Integer> {
        void writePrimitive(NetOutput output, int value) throws IOException;

        @Deprecated
        @Override
        default void write(NetOutput output, Integer value) throws IOException {
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
