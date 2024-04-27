package org.geysermc.mcprotocollib.protocol.data.game.entity.metadata;

import io.netty.buffer.ByteBuf;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.math.vector.Vector3i;
import org.cloudburstmc.math.vector.Vector4f;
import org.cloudburstmc.nbt.NbtMap;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.data.game.entity.metadata.type.*;
import org.geysermc.mcprotocollib.protocol.data.game.entity.object.Direction;
import org.geysermc.mcprotocollib.protocol.data.game.entity.type.PaintingType;
import org.geysermc.mcprotocollib.protocol.data.game.item.ItemStack;
import org.geysermc.mcprotocollib.protocol.data.game.level.particle.Particle;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Getter
public class MetadataType<T> {
    private static final List<MetadataType<?>> VALUES = new ArrayList<>();
    protected final int id;
    protected final Reader<T> reader;
    protected final Writer<T> writer;
    protected final EntityMetadataFactory<T> metadataFactory;

    protected MetadataType(Reader<T> reader, Writer<T> writer, EntityMetadataFactory<T> metadataFactory) {
        this.id = VALUES.size();
        this.reader = reader;
        this.writer = writer;
        this.metadataFactory = metadataFactory;

        VALUES.add(this);
    }

    public static MetadataType<?> read(ByteBuf in, MinecraftCodecHelper helper) {
        int id = helper.readVarInt(in);
        if (id >= VALUES.size()) {
            throw new IllegalArgumentException("Received id " + id + " for MetadataType when the maximum was " + VALUES.size() + "!");
        }

        return VALUES.get(id);
    }

    public static MetadataType<?> from(int id) {
        return VALUES.get(id);
    }

    public static int size() {
        return VALUES.size();
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
