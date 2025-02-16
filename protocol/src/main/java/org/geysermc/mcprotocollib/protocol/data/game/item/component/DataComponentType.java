package org.geysermc.mcprotocollib.protocol.data.game.item.component;

import io.netty.buffer.ByteBuf;
import lombok.Getter;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.KeyPattern;

@Getter
public class DataComponentType<T> {
    protected final int id;
    protected final Key key;
    protected final Reader<T> reader;
    protected final Writer<T> writer;
    protected final DataComponentFactory<T> dataComponentFactory;

    protected DataComponentType(int id, @KeyPattern String key, Reader<T> reader, Writer<T> writer, DataComponentFactory<T> dataComponentFactory) {
        this.id = id;
        this.key = Key.key(key);
        this.reader = reader;
        this.writer = writer;
        this.dataComponentFactory = dataComponentFactory;
    }

    public DataComponent<T, ? extends DataComponentType<T>> readDataComponent(ByteBuf input) {
        return this.dataComponentFactory.create(this, this.reader.read(input));
    }

    public DataComponent<T, ? extends DataComponentType<T>> readNullDataComponent() {
        return this.dataComponentFactory.create(this, null);
    }

    public void writeDataComponent(ByteBuf output, T value) {
        this.writer.write(output, value);
    }

    @FunctionalInterface
    public interface Reader<V> {
        V read(ByteBuf input);
    }

    @FunctionalInterface
    public interface Writer<V> {
        void write(ByteBuf output, V value);
    }

    @FunctionalInterface
    public interface BasicReader<V> extends Reader<V> {
        V read(ByteBuf input);
    }

    @FunctionalInterface
    public interface BasicWriter<V> extends Writer<V> {
        void write(ByteBuf output, V Value);
    }

    @FunctionalInterface
    public interface DataComponentFactory<V> {
        DataComponent<V, ? extends DataComponentType<V>> create(DataComponentType<V> type, V value);
    }

    @Override
    public String toString() {
        return "DataComponentType(id=" + id + " , key=" + key.asString() + ")";
    }
}
