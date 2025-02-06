package org.geysermc.mcprotocollib.protocol.data.game.entity.metadata;

import io.netty.buffer.ByteBuf;
import lombok.Getter;

@Getter
public class MetadataType<T> {
    protected final int id;
    protected final Reader<T> reader;
    protected final Writer<T> writer;
    protected final EntityMetadataFactory<T> metadataFactory;

    protected MetadataType(int id, Reader<T> reader, Writer<T> writer, EntityMetadataFactory<T> metadataFactory) {
        this.id = id;
        this.reader = reader;
        this.writer = writer;
        this.metadataFactory = metadataFactory;
    }

    public EntityMetadata<T, ? extends MetadataType<T>> readMetadata(ByteBuf input, int id) {
        return this.metadataFactory.create(id, this, this.reader.read(input));
    }

    public void writeMetadata(ByteBuf output, T value) {
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
    public interface BasicReader<V> extends MetadataType.Reader<V> {
        V read(ByteBuf input);
    }

    @FunctionalInterface
    public interface BasicWriter<V> extends MetadataType.Writer<V> {
        void write(ByteBuf output, V value);
    }

    @FunctionalInterface
    public interface EntityMetadataFactory<V> {
        EntityMetadata<V, ? extends MetadataType<V>> create(int id, MetadataType<V> type, V value);
    }
}
