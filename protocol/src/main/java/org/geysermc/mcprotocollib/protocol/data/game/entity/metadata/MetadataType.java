package org.geysermc.mcprotocollib.protocol.data.game.entity.metadata;

import io.netty.buffer.ByteBuf;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.math.vector.Vector3i;
import org.cloudburstmc.math.vector.Vector4f;
import org.cloudburstmc.nbt.NbtMap;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;
import org.geysermc.mcprotocollib.protocol.data.game.Holder;
import org.geysermc.mcprotocollib.protocol.data.game.entity.metadata.type.BooleanEntityMetadata;
import org.geysermc.mcprotocollib.protocol.data.game.entity.metadata.type.ByteEntityMetadata;
import org.geysermc.mcprotocollib.protocol.data.game.entity.metadata.type.FloatEntityMetadata;
import org.geysermc.mcprotocollib.protocol.data.game.entity.metadata.type.IntEntityMetadata;
import org.geysermc.mcprotocollib.protocol.data.game.entity.metadata.type.LongEntityMetadata;
import org.geysermc.mcprotocollib.protocol.data.game.entity.metadata.type.ObjectEntityMetadata;
import org.geysermc.mcprotocollib.protocol.data.game.entity.object.Direction;
import org.geysermc.mcprotocollib.protocol.data.game.item.ItemStack;
import org.geysermc.mcprotocollib.protocol.data.game.level.particle.Particle;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Getter
public class MetadataType<T> {
    private static final List<MetadataType<?>> VALUES = new ArrayList<>();

    public static final ByteMetadataType BYTE = new ByteMetadataType(ByteBuf::readByte, ByteBuf::writeByte, ByteEntityMetadata::new);
    public static final IntMetadataType INT = new IntMetadataType(MinecraftTypes::readVarInt, MinecraftTypes::writeVarInt, IntEntityMetadata::new);
    public static final LongMetadataType LONG = new LongMetadataType(MinecraftTypes::readVarLong, MinecraftTypes::writeVarLong, LongEntityMetadata::new);
    public static final FloatMetadataType FLOAT = new FloatMetadataType(ByteBuf::readFloat, ByteBuf::writeFloat, FloatEntityMetadata::new);
    public static final MetadataType<String> STRING = new MetadataType<>(MinecraftTypes::readString, MinecraftTypes::writeString, ObjectEntityMetadata::new);
    public static final MetadataType<Component> CHAT = new MetadataType<>(MinecraftTypes::readComponent, MinecraftTypes::writeComponent, ObjectEntityMetadata::new);
    public static final MetadataType<Optional<Component>> OPTIONAL_CHAT = new MetadataType<>(optionalReader(MinecraftTypes::readComponent), optionalWriter(MinecraftTypes::writeComponent), ObjectEntityMetadata::new);
    public static final MetadataType<ItemStack> ITEM = new MetadataType<>(MinecraftTypes::readOptionalItemStack, MinecraftTypes::writeOptionalItemStack, ObjectEntityMetadata::new);
    public static final BooleanMetadataType BOOLEAN = new BooleanMetadataType(ByteBuf::readBoolean, ByteBuf::writeBoolean, BooleanEntityMetadata::new);
    public static final MetadataType<Vector3f> ROTATION = new MetadataType<>(MinecraftTypes::readRotation, MinecraftTypes::writeRotation, ObjectEntityMetadata::new);
    public static final MetadataType<Vector3i> POSITION = new MetadataType<>(MinecraftTypes::readPosition, MinecraftTypes::writePosition, ObjectEntityMetadata::new);
    public static final MetadataType<Optional<Vector3i>> OPTIONAL_POSITION = new MetadataType<>(optionalReader(MinecraftTypes::readPosition), optionalWriter(MinecraftTypes::writePosition), ObjectEntityMetadata::new);
    public static final MetadataType<Direction> DIRECTION = new MetadataType<>(MinecraftTypes::readDirection, MinecraftTypes::writeDirection, ObjectEntityMetadata::new);
    public static final MetadataType<Optional<UUID>> OPTIONAL_UUID = new MetadataType<>(optionalReader(MinecraftTypes::readUUID), optionalWriter(MinecraftTypes::writeUUID), ObjectEntityMetadata::new);
    public static final IntMetadataType BLOCK_STATE = new IntMetadataType(MinecraftTypes::readVarInt, MinecraftTypes::writeVarInt, IntEntityMetadata::new);
    public static final IntMetadataType OPTIONAL_BLOCK_STATE = new IntMetadataType(MinecraftTypes::readVarInt, MinecraftTypes::writeVarInt, IntEntityMetadata::new);
    public static final MetadataType<NbtMap> NBT_TAG = new MetadataType<>(MinecraftTypes::readCompoundTag, MinecraftTypes::writeAnyTag, ObjectEntityMetadata::new);
    public static final MetadataType<Particle> PARTICLE = new MetadataType<>(MinecraftTypes::readParticle, MinecraftTypes::writeParticle, ObjectEntityMetadata::new);
    public static final MetadataType<List<Particle>> PARTICLES = new MetadataType<>(listReader(MinecraftTypes::readParticle), listWriter(MinecraftTypes::writeParticle), ObjectEntityMetadata::new);
    public static final MetadataType<VillagerData> VILLAGER_DATA = new MetadataType<>(MinecraftTypes::readVillagerData, MinecraftTypes::writeVillagerData, ObjectEntityMetadata::new);
    public static final OptionalIntMetadataType OPTIONAL_VARINT = new OptionalIntMetadataType(ObjectEntityMetadata::new);
    public static final MetadataType<Pose> POSE = new MetadataType<>(MinecraftTypes::readPose, MinecraftTypes::writePose, ObjectEntityMetadata::new);
    public static final IntMetadataType CAT_VARIANT = new IntMetadataType(MinecraftTypes::readVarInt, MinecraftTypes::writeVarInt, IntEntityMetadata::new);
    public static final MetadataType<Holder<WolfVariant>> WOLF_VARIANT = new MetadataType<>(MinecraftTypes::readWolfVariant, MinecraftTypes::writeWolfVariant, ObjectEntityMetadata::new);
    public static final IntMetadataType FROG_VARIANT = new IntMetadataType(MinecraftTypes::readVarInt, MinecraftTypes::writeVarInt, IntEntityMetadata::new);
    public static final MetadataType<Optional<GlobalPos>> OPTIONAL_GLOBAL_POS = new MetadataType<>(optionalReader(MinecraftTypes::readGlobalPos), optionalWriter(MinecraftTypes::writeGlobalPos), ObjectEntityMetadata::new);
    public static final MetadataType<Holder<PaintingVariant>> PAINTING_VARIANT = new MetadataType<>(MinecraftTypes::readPaintingVariant, MinecraftTypes::writePaintingVariant, ObjectEntityMetadata::new);
    public static final MetadataType<SnifferState> SNIFFER_STATE = new MetadataType<>(MinecraftTypes::readSnifferState, MinecraftTypes::writeSnifferState, ObjectEntityMetadata::new);
    public static final MetadataType<ArmadilloState> ARMADILLO_STATE = new MetadataType<>(MinecraftTypes::readArmadilloState, MinecraftTypes::writeArmadilloState, ObjectEntityMetadata::new);
    public static final MetadataType<Vector3f> VECTOR3 = new MetadataType<>(MinecraftTypes::readRotation, MinecraftTypes::writeRotation, ObjectEntityMetadata::new);
    public static final MetadataType<Vector4f> QUATERNION = new MetadataType<>(MinecraftTypes::readQuaternion, MinecraftTypes::writeQuaternion, ObjectEntityMetadata::new);

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
    public interface BasicReader<V> extends Reader<V> {
        V read(ByteBuf input);
    }

    @FunctionalInterface
    public interface BasicWriter<V> extends Writer<V> {
        void write(ByteBuf output, V value);
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
        return (input) -> {
            if (!input.readBoolean()) {
                return Optional.empty();
            }

            return Optional.of(reader.read(input));
        };
    }

    private static <T> BasicWriter<Optional<T>> optionalWriter(BasicWriter<T> writer) {
        return (output, value) -> {
            output.writeBoolean(value.isPresent());
            value.ifPresent(t -> writer.write(output, t));
        };
    }

    private static <T> Writer<Optional<T>> optionalWriter(Writer<T> writer) {
        return (output, value) -> {
            output.writeBoolean(value.isPresent());
            value.ifPresent(t -> writer.write(output, t));
        };
    }

    private static <T> Reader<List<T>> listReader(Reader<T> reader) {
        return (input) -> {
            List<T> ret = new ArrayList<>();
            int size = MinecraftTypes.readVarInt(input);
            for (int i = 0; i < size; i++) {
                ret.add(reader.read(input));
            }

            return ret;
        };
    }

    private static <T> Writer<List<T>> listWriter(Writer<T> writer) {
        return (output, value) -> {
            MinecraftTypes.writeVarInt(output, value.size());
            for (T object : value) {
                writer.write(output, object);
            }
        };
    }

    public static MetadataType<?> read(ByteBuf in) {
        int id = MinecraftTypes.readVarInt(in);
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
