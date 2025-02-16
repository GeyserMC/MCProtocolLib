package org.geysermc.mcprotocollib.protocol.data.game.item.component;

import io.netty.buffer.ByteBuf;
import net.kyori.adventure.key.KeyPattern;
import org.geysermc.mcprotocollib.protocol.data.game.item.component.type.IntDataComponent;

public class IntComponentType extends DataComponentType<Integer> {
    protected final IntReader primitiveReader;
    protected final IntWriter primitiveWriter;
    protected final IntDataComponentFactory primitiveFactory;

    protected IntComponentType(int id, @KeyPattern String key, IntReader reader, IntWriter writer, IntDataComponentFactory metadataFactory) {
        super(id, key, reader, writer, metadataFactory);

        this.primitiveReader = reader;
        this.primitiveWriter = writer;
        this.primitiveFactory = metadataFactory;
    }

    @Override
    public DataComponent<Integer, IntComponentType> readDataComponent(ByteBuf input) {
        return this.primitiveFactory.createPrimitive(this, this.primitiveReader.readPrimitive(input));
    }

    @Override
    public DataComponent<Integer, IntComponentType> readNullDataComponent() {
        return this.primitiveFactory.createPrimitive(this, null);
    }

    public void writeDataComponentPrimitive(ByteBuf output, int value) {
        this.primitiveWriter.writePrimitive(output, value);
    }

    @FunctionalInterface
    public interface IntReader extends Reader<Integer> {
        int readPrimitive(ByteBuf input);

        @Deprecated
        @Override
        default Integer read(ByteBuf input) {
            return this.readPrimitive(input);
        }
    }

    @FunctionalInterface
    public interface IntWriter extends Writer<Integer> {
        void writePrimitive(ByteBuf output, int value);

        @Deprecated
        @Override
        default void write(ByteBuf output, Integer value) {
            this.writePrimitive(output, value);
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

    @Override
    public String toString() {
        return "IntComponentType(id=" + id + " , key=" + key.asString() + ")";
    }
}
