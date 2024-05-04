package org.geysermc.mcprotocollib.protocol.data.game.item.component;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
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
    public DataComponent<Integer, IntComponentType> readDataComponent(ItemCodecByteBuf helper) {
        return this.primitiveFactory.createPrimitive(this, this.primitiveReader.readPrimitive(helper));
    }

    public void writeDataComponentPrimitive(ItemCodecByteBuf output, int value) {
        this.primitiveWriter.writePrimitive(output, value);
    }

    @FunctionalInterface
    public interface IntReader extends Reader<Integer> {
        int readPrimitive(ItemCodecByteBuf helper);

        @Deprecated
        @Override
        default Integer read(ItemCodecByteBuf helper) {
            return this.readPrimitive(helper);
        }
    }

    @FunctionalInterface
    public interface IntWriter extends Writer<Integer> {
        void writePrimitive(ItemCodecByteBuf helper, int value);

        @Deprecated
        @Override
        default void write(ItemCodecByteBuf helper, Integer value) {
            this.writePrimitive(helper, value);
        }
    }

    @FunctionalInterface
    public interface IntDataComponentFactory extends DataComponentFactory<Integer> {
        IntDataComponent createPrimitive(IntComponentType type, int value);

        @Deprecated
        @Override
        default DataComponent<Integer, IntComponentType> create(DataComponentType<Integer> type, Integer value) {
            throw new UnsupportedOperationException("Unsupported read method! Use primitive createPrimitive!");
        }
    }
}
