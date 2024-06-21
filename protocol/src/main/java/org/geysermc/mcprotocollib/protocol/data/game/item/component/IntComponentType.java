package org.geysermc.mcprotocollib.protocol.data.game.item.component;

import io.netty.buffer.ByteBuf;
import org.geysermc.mcprotocollib.protocol.data.game.item.component.type.IntDataComponent;

public class IntComponentType extends DataComponentType<Integer> {
    protected final IntReader primitiveReader;
    protected final IntWriter primitiveWriter;
    protected final IntDataComponentFactory primitiveFactory;

    protected IntComponentType(IntReader reader, IntWriter writer, IntDataComponentFactory metadataFactory) {
        super(reader, writer, metadataFactory);

        this.primitiveReader = reader;
        this.primitiveWriter = writer;
        this.primitiveFactory = metadataFactory;
    }

    @Override
    public DataComponent<Integer, IntComponentType> readDataComponent(ItemCodecHelper helper, ByteBuf input) {
        return this.primitiveFactory.createPrimitive(this, this.primitiveReader.readPrimitive(helper, input));
    }

    @Override
    public DataComponent<Integer, IntComponentType> readNullDataComponent() {
        return this.primitiveFactory.createPrimitive(this, null);
    }

    public void writeDataComponentPrimitive(ItemCodecHelper helper, ByteBuf output, int value) {
        this.primitiveWriter.writePrimitive(helper, output, value);
    }

    @FunctionalInterface
    public interface IntReader extends Reader<Integer> {
        int readPrimitive(ItemCodecHelper helper, ByteBuf input);

        @Deprecated
        @Override
        default Integer read(ItemCodecHelper helper, ByteBuf input) {
            return this.readPrimitive(helper, input);
        }
    }

    @FunctionalInterface
    public interface IntWriter extends Writer<Integer> {
        void writePrimitive(ItemCodecHelper helper, ByteBuf output, int value);

        @Deprecated
        @Override
        default void write(ItemCodecHelper helper, ByteBuf output, Integer value) {
            this.writePrimitive(helper, output, value);
        }
    }

    @FunctionalInterface
    public interface IntDataComponentFactory extends DataComponentFactory<Integer> {
        IntDataComponent createPrimitive(IntComponentType type, Integer value);

        @Deprecated
        @Override
        default DataComponent<Integer, IntComponentType> create(DataComponentType<Integer> type, Integer value) {
            throw new UnsupportedOperationException("Unsupported read method! Use primitive createPrimitive!");
        }
    }
}
