package com.github.steveice10.mc.protocol.data.game.entity.metadata;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.type.*;
import com.github.steveice10.mc.protocol.data.game.entity.object.Direction;
import com.github.steveice10.mc.protocol.data.game.entity.type.PaintingType;
import com.github.steveice10.mc.protocol.data.game.level.particle.Particle;
import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
import io.netty.buffer.ByteBuf;
import net.kyori.adventure.text.Component;
import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.math.vector.Vector3i;
import org.cloudburstmc.math.vector.Vector4f;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class MetadataTypes {
    private static final List<MetadataType<?>> VALUES = new ArrayList<>();
    public static final ByteMetadataType BYTE = register(new ByteMetadataType(nextId(), ByteBuf::readByte, ByteBuf::writeByte, ByteEntityMetadata::new));
    public static final IntMetadataType INT = register(new IntMetadataType(nextId(), MinecraftCodecHelper::readVarInt, MinecraftCodecHelper::writeVarInt, IntEntityMetadata::new));
    public static final LongMetadataType LONG = register(new LongMetadataType(nextId(), MinecraftCodecHelper::readVarLong, MinecraftCodecHelper::writeVarLong, LongEntityMetadata::new));
    public static final FloatMetadataType FLOAT = register(new FloatMetadataType(nextId(), ByteBuf::readFloat, ByteBuf::writeFloat, FloatEntityMetadata::new));
    public static final MetadataType<String> STRING = register(new MetadataType<>(nextId(), MinecraftCodecHelper::readString, MinecraftCodecHelper::writeString, ObjectEntityMetadata::new));
    public static final MetadataType<Component> CHAT = register(new MetadataType<>(nextId(), MinecraftCodecHelper::readComponent, MinecraftCodecHelper::writeComponent, ObjectEntityMetadata::new));
    public static final MetadataType<Optional<Component>> OPTIONAL_CHAT = register(new MetadataType<>(nextId(), optionalReader(MinecraftCodecHelper::readComponent), optionalWriter(MinecraftCodecHelper::writeComponent), ObjectEntityMetadata::new));
    public static final MetadataType<ItemStack> ITEM = register(new MetadataType<>(nextId(), MinecraftCodecHelper::readItemStack, MinecraftCodecHelper::writeItemStack, ObjectEntityMetadata::new));
    public static final BooleanMetadataType BOOLEAN = register(new BooleanMetadataType(nextId(), ByteBuf::readBoolean, ByteBuf::writeBoolean, BooleanEntityMetadata::new));
    public static final MetadataType<Vector3f> ROTATION = register(new MetadataType<>(nextId(), MinecraftCodecHelper::readRotation, MinecraftCodecHelper::writeRotation, ObjectEntityMetadata::new));
    public static final MetadataType<Vector3i> POSITION = register(new MetadataType<>(nextId(), MinecraftCodecHelper::readPosition, MinecraftCodecHelper::writePosition, ObjectEntityMetadata::new));
    public static final MetadataType<Optional<Vector3i>> OPTIONAL_POSITION = register(new MetadataType<>(nextId(), optionalReader(MinecraftCodecHelper::readPosition), optionalWriter(MinecraftCodecHelper::writePosition), ObjectEntityMetadata::new));
    public static final MetadataType<Direction> DIRECTION = register(new MetadataType<>(nextId(), MinecraftCodecHelper::readDirection, MinecraftCodecHelper::writeDirection, ObjectEntityMetadata::new));
    public static final MetadataType<Optional<UUID>> OPTIONAL_UUID = register(new MetadataType<>(nextId(), optionalReader(MinecraftCodecHelper::readUUID), optionalWriter(MinecraftCodecHelper::writeUUID), ObjectEntityMetadata::new));
    public static final IntMetadataType BLOCK_STATE = register(new IntMetadataType(nextId(), MinecraftCodecHelper::readVarInt, MinecraftCodecHelper::writeVarInt, IntEntityMetadata::new));
    public static final IntMetadataType OPTIONAL_BLOCK_STATE = register(new IntMetadataType(nextId(), MinecraftCodecHelper::readVarInt, MinecraftCodecHelper::writeVarInt, IntEntityMetadata::new));
    public static final MetadataType<CompoundTag> NBT_TAG = register(new MetadataType<>(nextId(), MinecraftCodecHelper::readAnyTag, MinecraftCodecHelper::writeAnyTag, ObjectEntityMetadata::new));
    public static final MetadataType<Particle> PARTICLE = register(new MetadataType<>(nextId(), MinecraftCodecHelper::readParticle, MinecraftCodecHelper::writeParticle, ObjectEntityMetadata::new));
    public static final MetadataType<VillagerData> VILLAGER_DATA = register(new MetadataType<>(nextId(), MinecraftCodecHelper::readVillagerData, MinecraftCodecHelper::writeVillagerData, ObjectEntityMetadata::new));
    public static final OptionalIntMetadataType OPTIONAL_VARINT = register(new OptionalIntMetadataType(nextId(), ObjectEntityMetadata::new));
    public static final MetadataType<Pose> POSE = register(new MetadataType<>(nextId(), MinecraftCodecHelper::readPose, MinecraftCodecHelper::writePose, ObjectEntityMetadata::new));
    public static final IntMetadataType CAT_VARIANT = register(new IntMetadataType(nextId(), MinecraftCodecHelper::readVarInt, MinecraftCodecHelper::writeVarInt, IntEntityMetadata::new));
    public static final IntMetadataType FROG_VARIANT = register(new IntMetadataType(nextId(), MinecraftCodecHelper::readVarInt, MinecraftCodecHelper::writeVarInt, IntEntityMetadata::new));
    public static final MetadataType<Optional<GlobalPos>> OPTIONAL_GLOBAL_POS = register(new MetadataType<>(nextId(), optionalReader(MinecraftCodecHelper::readGlobalPos), optionalWriter(MinecraftCodecHelper::writeGlobalPos), ObjectEntityMetadata::new));
    public static final MetadataType<PaintingType> PAINTING_VARIANT = register(new MetadataType<>(nextId(), MinecraftCodecHelper::readPaintingType, MinecraftCodecHelper::writePaintingType, ObjectEntityMetadata::new));
    public static final MetadataType<SnifferState> SNIFFER_STATE = register(new MetadataType<>(nextId(), MinecraftCodecHelper::readSnifferState, MinecraftCodecHelper::writeSnifferState, ObjectEntityMetadata::new));
    public static final MetadataType<Vector3f> VECTOR3 = register(new MetadataType<>(nextId(), MinecraftCodecHelper::readRotation, MinecraftCodecHelper::writeRotation, ObjectEntityMetadata::new));
    public static final MetadataType<Vector4f> QUATERNION = register(new MetadataType<>(nextId(), MinecraftCodecHelper::readQuaternion, MinecraftCodecHelper::writeQuaternion, ObjectEntityMetadata::new));

    private MetadataTypes() {
    }

    private static <T extends MetadataType<?>> T register(T value) {
        VALUES.add(value);
        return value;
    }

    private static int nextId() {
        return VALUES.size();
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
        return (helper, input) -> {
            if (!input.readBoolean()) {
                return Optional.empty();
            }

            return Optional.of(reader.read(helper, input));
        };
    }

    private static <T> MetadataType.BasicWriter<Optional<T>> optionalWriter(MetadataType.BasicWriter<T> writer) {
        return (ouput, value) -> {
            ouput.writeBoolean(value.isPresent());
            if (value.isPresent()) {
                writer.write(ouput, value.get());
            }
        };
    }

    private static <T> MetadataType.Writer<Optional<T>> optionalWriter(MetadataType.Writer<T> writer) {
        return (helper, ouput, value) -> {
            ouput.writeBoolean(value.isPresent());
            if (value.isPresent()) {
                writer.write(helper, ouput, value.get());
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
