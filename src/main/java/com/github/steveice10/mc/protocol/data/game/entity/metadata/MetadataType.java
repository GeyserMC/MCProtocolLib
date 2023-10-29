package com.github.steveice10.mc.protocol.data.game.entity.metadata;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.type.*;
import com.github.steveice10.mc.protocol.data.game.entity.object.Direction;
import com.github.steveice10.mc.protocol.data.game.entity.type.PaintingType;
import com.github.steveice10.mc.protocol.data.game.level.particle.Particle;
import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
import io.netty.buffer.ByteBuf;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.math.vector.Vector3i;
import org.cloudburstmc.math.vector.Vector4f;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Getter
public class MetadataType<T> {
    protected final int id;
    protected final Reader<T> reader;
    protected final Writer<T> writer;
    protected final EntityMetadataFactory<T> metadataFactory;

    protected MetadataType(int id, BasicReader<T> reader, BasicWriter<T> writer, EntityMetadataFactory<T> metadataFactory) {
        this.id = id;
        this.reader = reader;
        this.writer = writer;
        this.metadataFactory = metadataFactory;
    }

    protected MetadataType(int id, HelperReader<T> reader, HelperWriter<T> writer, EntityMetadataFactory<T> metadataFactory) {
        this.id = id;
        this.reader = reader;
        this.writer = writer;
        this.metadataFactory = metadataFactory;
    }
    public EntityMetadata<T, ? extends MetadataType<T>> readMetadata(MinecraftCodecHelper helper, ByteBuf input, int id) throws IOException {
        return this.metadataFactory.create(id, this, this.reader.read(helper, input));
    }

    public void writeMetadata(MinecraftCodecHelper helper, ByteBuf output, T value) throws IOException {
        this.writer.write(helper, output, value);
    }

    public interface Reader<V> {
        V read(ByteBuf input) throws IOException;

        V read(MinecraftCodecHelper helper, ByteBuf input) throws IOException;
    }

    public interface Writer<V> {
        void write(ByteBuf output, V value) throws IOException;

        void write(MinecraftCodecHelper helper, ByteBuf output, V value) throws IOException;
    }

    @FunctionalInterface
    public interface BasicReader<V> extends Reader<V> {
        V read(ByteBuf input) throws IOException;

        default V read(MinecraftCodecHelper helper, ByteBuf input) throws IOException {
            return this.read(input);
        }
    }

    @FunctionalInterface
    public interface BasicWriter<V> extends Writer<V> {
        void write(ByteBuf output, V value) throws IOException;

        default void write(MinecraftCodecHelper helper, ByteBuf output, V value) throws IOException {
            this.write(output, value);
        }
    }

    @FunctionalInterface
    public interface HelperReader<V> extends Reader<V> {
        default V read(ByteBuf input) throws IOException {
            throw new UnsupportedOperationException("This reader needs a codec helper!");
        }

        V read(MinecraftCodecHelper helper, ByteBuf input) throws IOException;
    }

    @FunctionalInterface
    public interface HelperWriter<V> extends Writer<V> {
        default void write(ByteBuf output, V value) throws IOException {
            throw new UnsupportedOperationException("This writer needs a codec helper!");
        }

        void write(MinecraftCodecHelper helper, ByteBuf output, V value) throws IOException;
    }

    @FunctionalInterface
    public interface EntityMetadataFactory<V> {
        EntityMetadata<V, ? extends MetadataType<V>> create(int id, MetadataType<V> type, V value);
    }
}
