package com.github.steveice10.mc.protocol.data.game.entity.metadata;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.type.BooleanEntityMetadata;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.type.ByteEntityMetadata;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.type.FloatEntityMetadata;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.type.IntEntityMetadata;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.type.LongEntityMetadata;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.type.ObjectEntityMetadata;
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
    private static final List<MetadataType<?>> VALUES = new ArrayList<>();

    public static final ByteMetadataType BYTE = new ByteMetadataType(ByteBuf::readByte, ByteBuf::writeByte, ByteEntityMetadata::new);
    public static final IntMetadataType INT = new IntMetadataType(MinecraftCodecHelper::readVarInt, MinecraftCodecHelper::writeVarInt, IntEntityMetadata::new);
    public static final LongMetadataType LONG = new LongMetadataType(MinecraftCodecHelper::readVarLong, MinecraftCodecHelper::writeVarLong, LongEntityMetadata::new);
    public static final FloatMetadataType FLOAT = new FloatMetadataType(ByteBuf::readFloat, ByteBuf::writeFloat, FloatEntityMetadata::new);
    public static final MetadataType<String> STRING = new MetadataType<>(MinecraftCodecHelper::readString, MinecraftCodecHelper::writeString, ObjectEntityMetadata::new);
    public static final MetadataType<Component> CHAT = new MetadataType<>(MinecraftCodecHelper::readComponent, MinecraftCodecHelper::writeComponent, ObjectEntityMetadata::new);
    public static final MetadataType<Optional<Component>> OPTIONAL_CHAT = new MetadataType<>(optionalReader(MinecraftCodecHelper::readComponent), optionalWriter(MinecraftCodecHelper::writeComponent), ObjectEntityMetadata::new);
    public static final MetadataType<ItemStack> ITEM = new MetadataType<>(MinecraftCodecHelper::readItemStack, MinecraftCodecHelper::writeItemStack, ObjectEntityMetadata::new);
    public static final BooleanMetadataType BOOLEAN = new BooleanMetadataType(ByteBuf::readBoolean, ByteBuf::writeBoolean, BooleanEntityMetadata::new);
    public static final MetadataType<Vector3f> ROTATION = new MetadataType<>(MinecraftCodecHelper::readRotation, MinecraftCodecHelper::writeRotation, ObjectEntityMetadata::new);
    public static final MetadataType<Vector3i> POSITION = new MetadataType<>(MinecraftCodecHelper::readPosition, MinecraftCodecHelper::writePosition, ObjectEntityMetadata::new);
    public static final MetadataType<Optional<Vector3i>> OPTIONAL_POSITION = new MetadataType<>(optionalReader(MinecraftCodecHelper::readPosition), optionalWriter(MinecraftCodecHelper::writePosition), ObjectEntityMetadata::new);
    public static final MetadataType<Direction> DIRECTION = new MetadataType<>(MinecraftCodecHelper::readDirection, MinecraftCodecHelper::writeDirection, ObjectEntityMetadata::new);
    public static final MetadataType<Optional<UUID>> OPTIONAL_UUID = new MetadataType<>(optionalReader(MinecraftCodecHelper::readUUID), optionalWriter(MinecraftCodecHelper::writeUUID), ObjectEntityMetadata::new);
    public static final IntMetadataType BLOCK_STATE = new IntMetadataType(MinecraftCodecHelper::readVarInt, MinecraftCodecHelper::writeVarInt, IntEntityMetadata::new);
    public static final IntMetadataType OPTIONAL_BLOCK_STATE = new IntMetadataType(MinecraftCodecHelper::readVarInt, MinecraftCodecHelper::writeVarInt, IntEntityMetadata::new);
    public static final MetadataType<CompoundTag> NBT_TAG = new MetadataType<>(MinecraftCodecHelper::readAnyTag, MinecraftCodecHelper::writeAnyTag, ObjectEntityMetadata::new);
    public static final MetadataType<Particle> PARTICLE = new MetadataType<>(MinecraftCodecHelper::readParticle, MinecraftCodecHelper::writeParticle, ObjectEntityMetadata::new);
    public static final MetadataType<VillagerData> VILLAGER_DATA = new MetadataType<>(MinecraftCodecHelper::readVillagerData, MinecraftCodecHelper::writeVillagerData, ObjectEntityMetadata::new);
    public static final OptionalIntMetadataType OPTIONAL_VARINT = new OptionalIntMetadataType(ObjectEntityMetadata::new);
    public static final MetadataType<Pose> POSE = new MetadataType<>(MinecraftCodecHelper::readPose, MinecraftCodecHelper::writePose, ObjectEntityMetadata::new);
    public static final IntMetadataType CAT_VARIANT = new IntMetadataType(MinecraftCodecHelper::readVarInt, MinecraftCodecHelper::writeVarInt, IntEntityMetadata::new);
    public static final IntMetadataType FROG_VARIANT = new IntMetadataType(MinecraftCodecHelper::readVarInt, MinecraftCodecHelper::writeVarInt, IntEntityMetadata::new);
    public static final MetadataType<Optional<GlobalPos>> OPTIONAL_GLOBAL_POS = new MetadataType<>(optionalReader(MinecraftCodecHelper::readGlobalPos), optionalWriter(MinecraftCodecHelper::writeGlobalPos), ObjectEntityMetadata::new);
    public static final MetadataType<PaintingType> PAINTING_VARIANT = new MetadataType<>(MinecraftCodecHelper::readPaintingType, MinecraftCodecHelper::writePaintingType, ObjectEntityMetadata::new);
    public static final MetadataType<SnifferState> SNIFFER_STATE = new MetadataType<>(MinecraftCodecHelper::readSnifferState, MinecraftCodecHelper::writeSnifferState, ObjectEntityMetadata::new);
    public static final MetadataType<Vector3f> VECTOR3 = new MetadataType<>(MinecraftCodecHelper::readRotation, MinecraftCodecHelper::writeRotation, ObjectEntityMetadata::new);
    public static final MetadataType<Vector4f> QUATERNION = new MetadataType<>(MinecraftCodecHelper::readQuaternion, MinecraftCodecHelper::writeQuaternion, ObjectEntityMetadata::new);

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
    public interface BasicReader<V> extends Reader<V> {
        V read(ByteBuf input);

        default V read(MinecraftCodecHelper helper, ByteBuf input) {
            return this.read(input);
        }
    }

    @FunctionalInterface
    public interface BasicWriter<V> extends Writer<V> {
        void write(ByteBuf output, V value);

        default void write(MinecraftCodecHelper helper, ByteBuf output, V value) {
            this.write(output, value);
        }
    }

    @FunctionalInterface
    public interface EntityMetadataFactory<V> {
        EntityMetadata<V, ? extends MetadataType<V>> create(int id, MetadataType<V> type, V value);
    }

    private static <T> BasicReader<Optional<T>> optionalReader(BasicReader<T> reader) {
        return input -> {
            if (!input.readBoolean()) {
                return Optional.empty();
            }

            return Optional.of(reader.read(input));
        };
    }

    private static <T> Reader<Optional<T>> optionalReader(Reader<T> reader) {
        return (helper, input) -> {
            if (!input.readBoolean()) {
                return Optional.empty();
            }

            return Optional.of(reader.read(helper, input));
        };
    }

    private static <T> BasicWriter<Optional<T>> optionalWriter(BasicWriter<T> writer) {
        return (output, value) -> {
              output.writeBoolean(value.isPresent());
              if (value.isPresent()) {
                  writer.write(output, value.get());
              }
        };
    }

    private static <T> Writer<Optional<T>> optionalWriter(Writer<T> writer) {
        return (helper, output, value) -> {
            output.writeBoolean(value.isPresent());
            if (value.isPresent()) {
                writer.write(helper, output, value.get());
            }
        };
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
}
