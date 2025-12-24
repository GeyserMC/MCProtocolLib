package org.geysermc.mcprotocollib.protocol.data.game.entity.metadata;

import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.ints.Int2ObjectFunction;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.cloudburstmc.math.imaginary.Quaternionf;
import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.math.vector.Vector3i;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;
import org.geysermc.mcprotocollib.protocol.data.game.Holder;
import org.geysermc.mcprotocollib.protocol.data.game.entity.metadata.type.BooleanEntityMetadata;
import org.geysermc.mcprotocollib.protocol.data.game.entity.metadata.type.ByteEntityMetadata;
import org.geysermc.mcprotocollib.protocol.data.game.entity.metadata.type.FloatEntityMetadata;
import org.geysermc.mcprotocollib.protocol.data.game.entity.metadata.type.IntEntityMetadata;
import org.geysermc.mcprotocollib.protocol.data.game.entity.metadata.type.LongEntityMetadata;
import org.geysermc.mcprotocollib.protocol.data.game.entity.metadata.type.ObjectEntityMetadata;
import org.geysermc.mcprotocollib.protocol.data.game.entity.object.Direction;
import org.geysermc.mcprotocollib.protocol.data.game.entity.player.ResolvableProfile;
import org.geysermc.mcprotocollib.protocol.data.game.item.ItemStack;
import org.geysermc.mcprotocollib.protocol.data.game.level.particle.Particle;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Getter
public class MetadataTypes {
    private static final List<MetadataType<?>> VALUES = new ArrayList<>();

    public static final ByteMetadataType BYTE = register(id -> new ByteMetadataType(id, ByteBuf::readByte, ByteBuf::writeByte, ByteEntityMetadata::new));
    public static final IntMetadataType INT = register(id -> new IntMetadataType(id, MinecraftTypes::readVarInt, MinecraftTypes::writeVarInt, IntEntityMetadata::new));
    public static final LongMetadataType LONG = register(id -> new LongMetadataType(id, MinecraftTypes::readVarLong, MinecraftTypes::writeVarLong, LongEntityMetadata::new));
    public static final FloatMetadataType FLOAT = register(id -> new FloatMetadataType(id, ByteBuf::readFloat, ByteBuf::writeFloat, FloatEntityMetadata::new));
    public static final MetadataType<String> STRING = register(id -> new MetadataType<>(id, MinecraftTypes::readString, MinecraftTypes::writeString, ObjectEntityMetadata::new));
    public static final MetadataType<Component> COMPONENT = register(id -> new MetadataType<>(id, MinecraftTypes::readComponent, MinecraftTypes::writeComponent, ObjectEntityMetadata::new));
    public static final MetadataType<Optional<Component>> OPTIONAL_COMPONENT = register(id -> new MetadataType<>(id, optionalReader(MinecraftTypes::readComponent), optionalWriter(MinecraftTypes::writeComponent), ObjectEntityMetadata::new));
    public static final MetadataType<ItemStack> ITEM_STACK = register(id -> new MetadataType<>(id, MinecraftTypes::readOptionalItemStack, MinecraftTypes::writeOptionalItemStack, ObjectEntityMetadata::new));
    public static final BooleanMetadataType BOOLEAN = register(id -> new BooleanMetadataType(id, ByteBuf::readBoolean, ByteBuf::writeBoolean, BooleanEntityMetadata::new));
    public static final MetadataType<Vector3f> ROTATIONS = register(id -> new MetadataType<>(id, MinecraftTypes::readRotation, MinecraftTypes::writeRotation, ObjectEntityMetadata::new));
    public static final MetadataType<Vector3i> BLOCK_POS = register(id -> new MetadataType<>(id, MinecraftTypes::readPosition, MinecraftTypes::writePosition, ObjectEntityMetadata::new));
    public static final MetadataType<Optional<Vector3i>> OPTIONAL_BLOCK_POS = register(id -> new MetadataType<>(id, optionalReader(MinecraftTypes::readPosition), optionalWriter(MinecraftTypes::writePosition), ObjectEntityMetadata::new));
    public static final MetadataType<Direction> DIRECTION = register(id -> new MetadataType<>(id, MinecraftTypes::readDirection, MinecraftTypes::writeDirection, ObjectEntityMetadata::new));
    public static final MetadataType<Optional<UUID>> OPTIONAL_LIVING_ENTITY_REFERENCE = register(id -> new MetadataType<>(id, optionalReader(MinecraftTypes::readUUID), optionalWriter(MinecraftTypes::writeUUID), ObjectEntityMetadata::new));
    public static final IntMetadataType BLOCK_STATE = register(id -> new IntMetadataType(id, MinecraftTypes::readVarInt, MinecraftTypes::writeVarInt, IntEntityMetadata::new));
    public static final IntMetadataType OPTIONAL_BLOCK_STATE = register(id -> new IntMetadataType(id, MinecraftTypes::readVarInt, MinecraftTypes::writeVarInt, IntEntityMetadata::new));
    public static final MetadataType<Particle> PARTICLE = register(id -> new MetadataType<>(id, MinecraftTypes::readParticle, MinecraftTypes::writeParticle, ObjectEntityMetadata::new));
    public static final MetadataType<List<Particle>> PARTICLES = register(id -> new MetadataType<>(id, listReader(MinecraftTypes::readParticle), listWriter(MinecraftTypes::writeParticle), ObjectEntityMetadata::new));
    public static final MetadataType<VillagerData> VILLAGER_DATA = register(id -> new MetadataType<>(id, MinecraftTypes::readVillagerData, MinecraftTypes::writeVillagerData, ObjectEntityMetadata::new));
    public static final OptionalIntMetadataType OPTIONAL_UNSIGNED_INT = register(id -> new OptionalIntMetadataType(id, ObjectEntityMetadata::new));
    public static final MetadataType<Pose> POSE = register(id -> new MetadataType<>(id, MinecraftTypes::readPose, MinecraftTypes::writePose, ObjectEntityMetadata::new));
    public static final IntMetadataType CAT_VARIANT = register(id -> new IntMetadataType(id, MinecraftTypes::readVarInt, MinecraftTypes::writeVarInt, IntEntityMetadata::new));
    public static final IntMetadataType COW_VARIANT = register(id -> new IntMetadataType(id, MinecraftTypes::readVarInt, MinecraftTypes::writeVarInt, IntEntityMetadata::new));
    public static final IntMetadataType WOLF_VARIANT = register(id -> new IntMetadataType(id, MinecraftTypes::readVarInt, MinecraftTypes::writeVarInt, IntEntityMetadata::new));
    public static final IntMetadataType WOLF_SOUND_VARIANT = register(id -> new IntMetadataType(id, MinecraftTypes::readVarInt, MinecraftTypes::writeVarInt, IntEntityMetadata::new));
    public static final IntMetadataType FROG_VARIANT = register(id -> new IntMetadataType(id, MinecraftTypes::readVarInt, MinecraftTypes::writeVarInt, IntEntityMetadata::new));
    public static final IntMetadataType PIG_VARIANT = register(id -> new IntMetadataType(id, MinecraftTypes::readVarInt, MinecraftTypes::writeVarInt, IntEntityMetadata::new));
    public static final IntMetadataType CHICKEN_VARIANT = register(id -> new IntMetadataType(id, MinecraftTypes::readVarInt, MinecraftTypes::writeVarInt, IntEntityMetadata::new));
    public static final IntMetadataType ZOMBIE_NAUTILUS_VARIANT = register(id -> new IntMetadataType(id, MinecraftTypes::readVarInt, MinecraftTypes::writeVarInt, IntEntityMetadata::new));
    public static final MetadataType<Optional<GlobalPos>> OPTIONAL_GLOBAL_POS = register(id -> new MetadataType<>(id, optionalReader(MinecraftTypes::readGlobalPos), optionalWriter(MinecraftTypes::writeGlobalPos), ObjectEntityMetadata::new));
    public static final MetadataType<Holder<PaintingVariant>> PAINTING_VARIANT = register(id -> new MetadataType<>(id, MinecraftTypes::readPaintingVariant, MinecraftTypes::writePaintingVariant, ObjectEntityMetadata::new));
    public static final MetadataType<SnifferState> SNIFFER_STATE = register(id -> new MetadataType<>(id, MinecraftTypes::readSnifferState, MinecraftTypes::writeSnifferState, ObjectEntityMetadata::new));
    public static final MetadataType<ArmadilloState> ARMADILLO_STATE = register(id -> new MetadataType<>(id, MinecraftTypes::readArmadilloState, MinecraftTypes::writeArmadilloState, ObjectEntityMetadata::new));
    public static final MetadataType<CopperGolemState> COPPER_GOLEM_STATE = register(id -> new MetadataType<>(id, MinecraftTypes::readCopperGolemState, MinecraftTypes::writeCopperGolemState, ObjectEntityMetadata::new));
    public static final MetadataType<WeatheringCopperState> WEATHERING_COPPER_STATE = register(id -> new MetadataType<>(id, MinecraftTypes::readWeatheringCopperState, MinecraftTypes::writeWeatheringCopperState, ObjectEntityMetadata::new));
    public static final MetadataType<Vector3f> VECTOR3 = register(id -> new MetadataType<>(id, MinecraftTypes::readRotation, MinecraftTypes::writeRotation, ObjectEntityMetadata::new));
    public static final MetadataType<Quaternionf> QUATERNION = register(id ->  new MetadataType<>(id, MinecraftTypes::readQuaternion, MinecraftTypes::writeQuaternion, ObjectEntityMetadata::new));
    public static final MetadataType<ResolvableProfile> RESOLVABLE_PROFILE = register(id -> new MetadataType<>(id, MinecraftTypes::readResolvableProfile, MinecraftTypes::writeResolvableProfile, ObjectEntityMetadata::new));
    public static final MetadataType<HumanoidArm> HUMANOID_ARM = register(id -> new MetadataType<>(id, MinecraftTypes::readHumanoidArm, MinecraftTypes::writeHumanoidArm, ObjectEntityMetadata::new));

    public static <T extends MetadataType<?>> T register(Int2ObjectFunction<T> factory) {
        T value = factory.apply(VALUES.size());
        VALUES.add(value);
        return value;
    }

    private static <T> MetadataType.BasicReader<Optional<T>> optionalReader(MetadataType.BasicReader<T> reader) {
        return input -> {
            if (!input.readBoolean()) {
                return Optional.empty();
            }

            return Optional.of(reader.read(input));
        };
    }

    private static <T> MetadataType.Reader<Optional<T>> optionalReader(MetadataType.Reader<T> reader) {
        return (input) -> {
            if (!input.readBoolean()) {
                return Optional.empty();
            }

            return Optional.of(reader.read(input));
        };
    }

    private static <T> MetadataType.BasicWriter<Optional<T>> optionalWriter(MetadataType.BasicWriter<T> writer) {
        return (output, value) -> {
            output.writeBoolean(value.isPresent());
            value.ifPresent(t -> writer.write(output, t));
        };
    }

    private static <T> MetadataType.Writer<Optional<T>> optionalWriter(MetadataType.Writer<T> writer) {
        return (output, value) -> {
            output.writeBoolean(value.isPresent());
            value.ifPresent(t -> writer.write(output, t));
        };
    }

    private static <T> MetadataType.Reader<List<T>> listReader(MetadataType.Reader<T> reader) {
        return (input) -> {
            List<T> ret = new ArrayList<>();
            int size = MinecraftTypes.readVarInt(input);
            for (int i = 0; i < size; i++) {
                ret.add(reader.read(input));
            }

            return ret;
        };
    }

    private static <T> MetadataType.Writer<List<T>> listWriter(MetadataType.Writer<T> writer) {
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
