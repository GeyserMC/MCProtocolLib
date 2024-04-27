package org.geysermc.mcprotocollib.protocol.data.game.item.component;

import io.netty.buffer.ByteBuf;
import lombok.Getter;

@Getter
public class DataComponentType<T> {
    protected final int id;
    protected final Reader<T> reader;
    protected final Writer<T> writer;
    protected final DataComponentFactory<T> dataComponentFactory;

    protected DataComponentType(int id, Reader<T> reader, Writer<T> writer, DataComponentFactory<T> dataComponentFactory) {
        this.id = id;
        this.reader = reader;
        this.writer = writer;
        this.dataComponentFactory = dataComponentFactory;
    }

    public DataComponent<T, ? extends DataComponentType<T>> readDataComponent(ItemCodecHelper helper, ByteBuf input) {
        return this.dataComponentFactory.create(this, this.reader.read(helper, input));
    }

    public DataComponent<T, ? extends DataComponentType<T>> readNullDataComponent() {
        return this.dataComponentFactory.create(this, null);
    }

    public void writeDataComponent(ItemCodecHelper helper, ByteBuf output, T value) {
        this.writer.write(helper, output, value);
    }

    @FunctionalInterface
    public interface Reader<V> {
        V read(ItemCodecHelper helper, ByteBuf input);
    }

    @FunctionalInterface
    public interface Writer<V> {
        void write(ItemCodecHelper helper, ByteBuf output, V value);
    }

    @FunctionalInterface
    public interface DataComponentFactory<V> {
        DataComponent<V, ? extends DataComponentType<V>> create(DataComponentType<V> type, V value);
    }
}
