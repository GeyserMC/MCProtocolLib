package org.geysermc.mcprotocollib.protocol.data.game.entity.metadata;

import io.netty.buffer.ByteBuf;
import lombok.Getter;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;

import java.util.ArrayList;
import java.util.List;

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

    public EntityMetadata<T, ? extends MetadataType<T>> readMetadata(MinecraftCodecHelper helper, ByteBuf input, int id) {
        return this.metadataFactory.create(id, this, this.reader.read(helper, input));
    }

    public void writeMetadata(MinecraftCodecHelper helper, ByteBuf output, T value) {
        this.writer.write(helper, output, value);
    }

    @FunctionalInterface
    public interface Reader<V> {
        V read(MinecraftCodecHelper helper, ByteBuf input);
    }

    @FunctionalInterface
    public interface Writer<V> {
        void write(MinecraftCodecHelper helper, ByteBuf output, V value);
    }

    @FunctionalInterface
    public interface EntityMetadataFactory<V> {
        EntityMetadata<V, ? extends MetadataType<V>> create(int id, MetadataType<V> type, V value);
    }
}
