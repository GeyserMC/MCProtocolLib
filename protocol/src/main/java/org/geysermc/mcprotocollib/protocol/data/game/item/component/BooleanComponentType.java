package org.geysermc.mcprotocollib.protocol.data.game.item.component;

import org.geysermc.mcprotocollib.protocol.data.game.item.component.type.BooleanDataComponent;

public class BooleanComponentType extends DataComponentType<Boolean> {
    protected final BooleanReader primitiveReader;
    protected final BooleanWriter primitiveWriter;
    protected final BooleanDataComponentFactory primitiveFactory;

    protected BooleanComponentType(BooleanReader reader, BooleanWriter writer, BooleanDataComponentFactory metadataFactory) {
        super(reader, writer, metadataFactory);

        this.primitiveReader = reader;
        this.primitiveWriter = writer;
        this.primitiveFactory = metadataFactory;
    }

    @Override
    public DataComponent<Boolean, BooleanComponentType> readDataComponent(ItemCodecByteBuf input) {
        return this.primitiveFactory.createPrimitive(this, this.primitiveReader.readPrimitive(input));
    }

    public void writeDataComponentPrimitive(ItemCodecByteBuf output, boolean value) {
        this.primitiveWriter.writePrimitive(output, value);
    }

    @FunctionalInterface
    public interface BooleanReader extends Reader<Boolean> {
        boolean readPrimitive(ItemCodecByteBuf input);

        @Deprecated
        @Override
        default Boolean read(ItemCodecByteBuf input) {
            return this.readPrimitive(input);
        }
    }

    @FunctionalInterface
    public interface BooleanWriter extends Writer<Boolean> {
        void writePrimitive(ItemCodecByteBuf output, boolean value);

        @Deprecated
        @Override
        default void write(ItemCodecByteBuf output, Boolean value) {
            this.writePrimitive(output, value);
        }
    }

    @FunctionalInterface
    public interface BooleanDataComponentFactory extends DataComponentFactory<Boolean> {
        BooleanDataComponent createPrimitive(BooleanComponentType type, boolean value);

        @Deprecated
        @Override
        default DataComponent<Boolean, BooleanComponentType> create(DataComponentType<Boolean> type, Boolean value) {
            throw new UnsupportedOperationException("Unsupported read method! Use primitive createPrimitive!");
        }
    }
}
