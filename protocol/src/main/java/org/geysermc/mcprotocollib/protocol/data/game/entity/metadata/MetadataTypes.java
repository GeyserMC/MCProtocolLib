package org.geysermc.mcprotocollib.protocol.data.game.entity.metadata;

import io.netty.buffer.ByteBuf;
import net.kyori.adventure.text.Component;
import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.math.vector.Vector3i;
import org.cloudburstmc.math.vector.Vector4f;
import org.cloudburstmc.nbt.NbtMap;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
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

public class MetadataTypes {
    public static final ByteMetadataType BYTE = new ByteMetadataType(ByteBuf::readByte, ByteBuf::writeByte, ByteEntityMetadata::new);
    public static final IntMetadataType INT = new IntMetadataType(MinecraftCodecHelper::readVarInt, MinecraftCodecHelper::writeVarInt, IntEntityMetadata::new);
    public static final LongMetadataType LONG = new LongMetadataType(MinecraftCodecHelper::readVarLong, MinecraftCodecHelper::writeVarLong, LongEntityMetadata::new);
    public static final FloatMetadataType FLOAT = new FloatMetadataType(ByteBuf::readFloat, ByteBuf::writeFloat, FloatEntityMetadata::new);
    public static final MetadataType<String> STRING = new MetadataType<>(MinecraftCodecHelper::readString, MinecraftCodecHelper::writeString, ObjectEntityMetadata::new);
    public static final MetadataType<Component> CHAT = new MetadataType<>(MinecraftCodecHelper::readComponent, MinecraftCodecHelper::writeComponent, ObjectEntityMetadata::new);
    public static final MetadataType<Optional<Component>> OPTIONAL_CHAT = new MetadataType<>(optionalReader(MinecraftCodecHelper::readComponent), optionalWriter(MinecraftCodecHelper::writeComponent), ObjectEntityMetadata::new);
    public static final MetadataType<ItemStack> ITEM = new MetadataType<>(MinecraftCodecHelper::readOptionalItemStack, MinecraftCodecHelper::writeOptionalItemStack, ObjectEntityMetadata::new);
    public static final BooleanMetadataType BOOLEAN = new BooleanMetadataType(ByteBuf::readBoolean, ByteBuf::writeBoolean, BooleanEntityMetadata::new);
    public static final MetadataType<Vector3f> ROTATION = new MetadataType<>(MinecraftCodecHelper::readRotation, MinecraftCodecHelper::writeRotation, ObjectEntityMetadata::new);
    public static final MetadataType<Vector3i> POSITION = new MetadataType<>(MinecraftCodecHelper::readPosition, MinecraftCodecHelper::writePosition, ObjectEntityMetadata::new);
    public static final MetadataType<Optional<Vector3i>> OPTIONAL_POSITION = new MetadataType<>(optionalReader(MinecraftCodecHelper::readPosition), optionalWriter(MinecraftCodecHelper::writePosition), ObjectEntityMetadata::new);
    public static final MetadataType<Direction> DIRECTION = new MetadataType<>(MinecraftCodecHelper::readDirection, MinecraftCodecHelper::writeDirection, ObjectEntityMetadata::new);
    public static final MetadataType<Optional<UUID>> OPTIONAL_UUID = new MetadataType<>(optionalReader(MinecraftCodecHelper::readUUID), optionalWriter(MinecraftCodecHelper::writeUUID), ObjectEntityMetadata::new);
    public static final IntMetadataType BLOCK_STATE = new IntMetadataType(MinecraftCodecHelper::readVarInt, MinecraftCodecHelper::writeVarInt, IntEntityMetadata::new);
    public static final IntMetadataType OPTIONAL_BLOCK_STATE = new IntMetadataType(MinecraftCodecHelper::readVarInt, MinecraftCodecHelper::writeVarInt, IntEntityMetadata::new);
    public static final MetadataType<NbtMap> NBT_TAG = new MetadataType<>(MinecraftCodecHelper::readCompoundTag, MinecraftCodecHelper::writeAnyTag, ObjectEntityMetadata::new);
    public static final MetadataType<Particle> PARTICLE = new MetadataType<>(MinecraftCodecHelper::readParticle, MinecraftCodecHelper::writeParticle, ObjectEntityMetadata::new);
    public static final MetadataType<List<Particle>> PARTICLES = new MetadataType<>(listReader(MinecraftCodecHelper::readParticle), listWriter(MinecraftCodecHelper::writeParticle), ObjectEntityMetadata::new);
    public static final MetadataType<VillagerData> VILLAGER_DATA = new MetadataType<>(MinecraftCodecHelper::readVillagerData, MinecraftCodecHelper::writeVillagerData, ObjectEntityMetadata::new);
    public static final OptionalIntMetadataType OPTIONAL_VARINT = new OptionalIntMetadataType(ObjectEntityMetadata::new);
    public static final MetadataType<Pose> POSE = new MetadataType<>(MinecraftCodecHelper::readPose, MinecraftCodecHelper::writePose, ObjectEntityMetadata::new);
    public static final IntMetadataType CAT_VARIANT = new IntMetadataType(MinecraftCodecHelper::readVarInt, MinecraftCodecHelper::writeVarInt, IntEntityMetadata::new);
    public static final IntMetadataType WOLF_VARIANT = new IntMetadataType(MinecraftCodecHelper::readVarInt, MinecraftCodecHelper::writeVarInt, IntEntityMetadata::new);
    public static final IntMetadataType FROG_VARIANT = new IntMetadataType(MinecraftCodecHelper::readVarInt, MinecraftCodecHelper::writeVarInt, IntEntityMetadata::new);
    public static final MetadataType<Optional<GlobalPos>> OPTIONAL_GLOBAL_POS = new MetadataType<>(optionalReader(MinecraftCodecHelper::readGlobalPos), optionalWriter(MinecraftCodecHelper::writeGlobalPos), ObjectEntityMetadata::new);
    public static final MetadataType<PaintingType> PAINTING_VARIANT = new MetadataType<>(MinecraftCodecHelper::readPaintingType, MinecraftCodecHelper::writePaintingType, ObjectEntityMetadata::new);
    public static final MetadataType<SnifferState> SNIFFER_STATE = new MetadataType<>(MinecraftCodecHelper::readSnifferState, MinecraftCodecHelper::writeSnifferState, ObjectEntityMetadata::new);
    public static final MetadataType<ArmadilloState> ARMADILLO_STATE = new MetadataType<>(MinecraftCodecHelper::readArmadilloState, MinecraftCodecHelper::writeArmadilloState, ObjectEntityMetadata::new);
    public static final MetadataType<Vector3f> VECTOR3 = new MetadataType<>(MinecraftCodecHelper::readRotation, MinecraftCodecHelper::writeRotation, ObjectEntityMetadata::new);
    public static final MetadataType<Vector4f> QUATERNION = new MetadataType<>(MinecraftCodecHelper::readQuaternion, MinecraftCodecHelper::writeQuaternion, ObjectEntityMetadata::new);

    private MetadataTypes() {
    }

    private static <T> MetadataType.Reader<Optional<T>> optionalReader(MetadataType.Reader<T> reader) {
        return (helper, input) -> {
            if (!input.readBoolean()) {
                return Optional.empty();
            }

            return Optional.of(reader.read(helper, input));
        };
    }

    private static <T> MetadataType.Writer<Optional<T>> optionalWriter(MetadataType.Writer<T> writer) {
        return (helper, output, value) -> {
            output.writeBoolean(value.isPresent());
            value.ifPresent(t -> writer.write(helper, output, t));
        };
    }

    private static <T> MetadataType.Reader<List<T>> listReader(MetadataType.Reader<T> reader) {
        return (helper, input) -> {
            List<T> ret = new ArrayList<>();
            int size = helper.readVarInt(input);
            for (int i = 0; i < size; i++) {
                ret.add(reader.read(helper, input));
            }

            return ret;
        };
    }

    private static <T> MetadataType.Writer<List<T>> listWriter(MetadataType.Writer<T> writer) {
        return (helper, output, value) -> {
            helper.writeVarInt(output, value.size());
            for (T object : value) {
                writer.write(helper, output, object);
            }
        };
    }
}
