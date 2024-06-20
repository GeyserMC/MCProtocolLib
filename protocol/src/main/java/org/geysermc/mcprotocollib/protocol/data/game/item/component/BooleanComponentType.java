package org.geysermc.mcprotocollib.protocol.data.game.item.component;

import io.netty.buffer.ByteBuf;
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
    public DataComponent<Boolean, BooleanComponentType> readDataComponent(ItemCodecHelper helper, ByteBuf input) {
        return this.primitiveFactory.createPrimitive(this, this.primitiveReader.readPrimitive(input));
    }

    @Override
    public DataComponent<Boolean, BooleanComponentType> readNullDataComponent() {
        return this.primitiveFactory.createPrimitive(this, null);
    }

    public void writeDataComponentPrimitive(ByteBuf output, boolean value) {
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
    public interface BooleanDataComponentFactory extends DataComponentFactory<Boolean> {
        BooleanDataComponent createPrimitive(BooleanComponentType type, Boolean value);

        @Deprecated
        @Override
        default DataComponent<Boolean, BooleanComponentType> create(DataComponentType<Boolean> type, Boolean value) {
            throw new UnsupportedOperationException("Unsupported read method! Use primitive createPrimitive!");
        }
    }
}
