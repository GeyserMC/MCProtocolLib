package org.geysermc.mcprotocollib.protocol.data.game.entity.metadata;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.math.vector.Vector3i;
import org.cloudburstmc.math.vector.Vector4f;
import org.cloudburstmc.nbt.NbtMap;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import org.geysermc.mcprotocollib.protocol.data.game.entity.metadata.type.BooleanEntityMetadata;
import org.geysermc.mcprotocollib.protocol.data.game.entity.metadata.type.ByteEntityMetadata;
import org.geysermc.mcprotocollib.protocol.data.game.entity.metadata.type.FloatEntityMetadata;
import org.geysermc.mcprotocollib.protocol.data.game.entity.metadata.type.IntEntityMetadata;
import org.geysermc.mcprotocollib.protocol.data.game.entity.metadata.type.LongEntityMetadata;
import org.geysermc.mcprotocollib.protocol.data.game.entity.metadata.type.ObjectEntityMetadata;
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

    public static final ByteMetadataType BYTE = new ByteMetadataType(MinecraftByteBuf::readByte, MinecraftByteBuf::writeByte, ByteEntityMetadata::new);
    public static final IntMetadataType INT = new IntMetadataType(MinecraftByteBuf::readVarInt, MinecraftByteBuf::writeVarInt, IntEntityMetadata::new);
    public static final LongMetadataType LONG = new LongMetadataType(MinecraftByteBuf::readVarLong, MinecraftByteBuf::writeVarLong, LongEntityMetadata::new);
    public static final FloatMetadataType FLOAT = new FloatMetadataType(MinecraftByteBuf::readFloat, MinecraftByteBuf::writeFloat, FloatEntityMetadata::new);
    public static final MetadataType<String> STRING = new MetadataType<>(MinecraftByteBuf::readString, MinecraftByteBuf::writeString, ObjectEntityMetadata::new);
    public static final MetadataType<Component> CHAT = new MetadataType<>(MinecraftByteBuf::readComponent, MinecraftByteBuf::writeComponent, ObjectEntityMetadata::new);
    public static final MetadataType<Optional<Component>> OPTIONAL_CHAT = new MetadataType<>(optionalReader(MinecraftByteBuf::readComponent), optionalWriter(MinecraftByteBuf::writeComponent), ObjectEntityMetadata::new);
    public static final MetadataType<ItemStack> ITEM = new MetadataType<>(MinecraftByteBuf::readOptionalItemStack, MinecraftByteBuf::writeOptionalItemStack, ObjectEntityMetadata::new);
    public static final BooleanMetadataType BOOLEAN = new BooleanMetadataType(MinecraftByteBuf::readBoolean, MinecraftByteBuf::writeBoolean, BooleanEntityMetadata::new);
    public static final MetadataType<Vector3f> ROTATION = new MetadataType<>(MinecraftByteBuf::readRotation, MinecraftByteBuf::writeRotation, ObjectEntityMetadata::new);
    public static final MetadataType<Vector3i> POSITION = new MetadataType<>(MinecraftByteBuf::readPosition, MinecraftByteBuf::writePosition, ObjectEntityMetadata::new);
    public static final MetadataType<Optional<Vector3i>> OPTIONAL_POSITION = new MetadataType<>(optionalReader(MinecraftByteBuf::readPosition), optionalWriter(MinecraftByteBuf::writePosition), ObjectEntityMetadata::new);
    public static final MetadataType<Direction> DIRECTION = new MetadataType<>(MinecraftByteBuf::readDirection, MinecraftByteBuf::writeDirection, ObjectEntityMetadata::new);
    public static final MetadataType<Optional<UUID>> OPTIONAL_UUID = new MetadataType<>(optionalReader(MinecraftByteBuf::readUUID), optionalWriter(MinecraftByteBuf::writeUUID), ObjectEntityMetadata::new);
    public static final IntMetadataType BLOCK_STATE = new IntMetadataType(MinecraftByteBuf::readVarInt, MinecraftByteBuf::writeVarInt, IntEntityMetadata::new);
    public static final IntMetadataType OPTIONAL_BLOCK_STATE = new IntMetadataType(MinecraftByteBuf::readVarInt, MinecraftByteBuf::writeVarInt, IntEntityMetadata::new);
    public static final MetadataType<NbtMap> NBT_TAG = new MetadataType<>(MinecraftByteBuf::readCompoundTag, MinecraftByteBuf::writeAnyTag, ObjectEntityMetadata::new);
    public static final MetadataType<Particle> PARTICLE = new MetadataType<>(MinecraftByteBuf::readParticle, MinecraftByteBuf::writeParticle, ObjectEntityMetadata::new);
    public static final MetadataType<List<Particle>> PARTICLES = new MetadataType<>(listReader(MinecraftByteBuf::readParticle), listWriter(MinecraftByteBuf::writeParticle), ObjectEntityMetadata::new);
    public static final MetadataType<VillagerData> VILLAGER_DATA = new MetadataType<>(MinecraftByteBuf::readVillagerData, MinecraftByteBuf::writeVillagerData, ObjectEntityMetadata::new);
    public static final OptionalIntMetadataType OPTIONAL_VARINT = new OptionalIntMetadataType(ObjectEntityMetadata::new);
    public static final MetadataType<Pose> POSE = new MetadataType<>(MinecraftByteBuf::readPose, MinecraftByteBuf::writePose, ObjectEntityMetadata::new);
    public static final IntMetadataType CAT_VARIANT = new IntMetadataType(MinecraftByteBuf::readVarInt, MinecraftByteBuf::writeVarInt, IntEntityMetadata::new);
    public static final IntMetadataType WOLF_VARIANT = new IntMetadataType(MinecraftByteBuf::readVarInt, MinecraftByteBuf::writeVarInt, IntEntityMetadata::new);
    public static final IntMetadataType FROG_VARIANT = new IntMetadataType(MinecraftByteBuf::readVarInt, MinecraftByteBuf::writeVarInt, IntEntityMetadata::new);
    public static final MetadataType<Optional<GlobalPos>> OPTIONAL_GLOBAL_POS = new MetadataType<>(optionalReader(MinecraftByteBuf::readGlobalPos), optionalWriter(MinecraftByteBuf::writeGlobalPos), ObjectEntityMetadata::new);
    public static final MetadataType<PaintingType> PAINTING_VARIANT = new MetadataType<>(MinecraftByteBuf::readPaintingType, MinecraftByteBuf::writePaintingType, ObjectEntityMetadata::new);
    public static final MetadataType<SnifferState> SNIFFER_STATE = new MetadataType<>(MinecraftByteBuf::readSnifferState, MinecraftByteBuf::writeSnifferState, ObjectEntityMetadata::new);
    public static final MetadataType<ArmadilloState> ARMADILLO_STATE = new MetadataType<>(MinecraftByteBuf::readArmadilloState, MinecraftByteBuf::writeArmadilloState, ObjectEntityMetadata::new);
    public static final MetadataType<Vector3f> VECTOR3 = new MetadataType<>(MinecraftByteBuf::readRotation, MinecraftByteBuf::writeRotation, ObjectEntityMetadata::new);
    public static final MetadataType<Vector4f> QUATERNION = new MetadataType<>(MinecraftByteBuf::readQuaternion, MinecraftByteBuf::writeQuaternion, ObjectEntityMetadata::new);

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

    public EntityMetadata<T, ? extends MetadataType<T>> readMetadata(MinecraftByteBuf input, int id) {
        return this.metadataFactory.create(id, this, this.reader.read(input));
    }

    public void writeMetadata(MinecraftByteBuf output, T value) {
        this.writer.write(output, value);
    }

    @FunctionalInterface
    public interface Reader<V> {
        V read(MinecraftByteBuf input);
    }

    @FunctionalInterface
    public interface Writer<V> {
        void write(MinecraftByteBuf output, V value);
    }

    @FunctionalInterface
    public interface EntityMetadataFactory<V> {
        EntityMetadata<V, ? extends MetadataType<V>> create(int id, MetadataType<V> type, V value);
    }

    private static <T> Reader<Optional<T>> optionalReader(Reader<T> reader) {
        return input -> {
            if (!input.readBoolean()) {
                return Optional.empty();
            }

            return Optional.of(reader.read(input));
        };
    }

    private static <T> Writer<Optional<T>> optionalWriter(Writer<T> writer) {
        return (output, value) -> {
            output.writeBoolean(value.isPresent());
            value.ifPresent(t -> writer.write(output, t));
        };
    }

    private static <T> Reader<List<T>> listReader(Reader<T> reader) {
        return input -> {
            List<T> ret = new ArrayList<>();
            int size = input.readVarInt();
            for (int i = 0; i < size; i++) {
                ret.add(reader.read(input));
            }

            return ret;
        };
    }

    private static <T> Writer<List<T>> listWriter(Writer<T> writer) {
        return (output, value) -> {
            output.writeVarInt(value.size());
            for (T object : value) {
                writer.write(output, object);
            }
        };
    }

    public static MetadataType<?> read(MinecraftByteBuf in) {
        int id = in.readVarInt();
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
