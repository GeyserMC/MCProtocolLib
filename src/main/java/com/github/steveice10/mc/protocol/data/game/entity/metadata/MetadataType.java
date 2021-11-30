package com.github.steveice10.mc.protocol.data.game.entity.metadata;

import com.github.steveice10.mc.protocol.data.DefaultComponentSerializer;
import com.github.steveice10.mc.protocol.data.game.NBT;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.type.BooleanEntityMetadata;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.type.ByteEntityMetadata;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.type.FloatEntityMetadata;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.type.IntEntityMetadata;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.type.ObjectEntityMetadata;
import com.github.steveice10.mc.protocol.data.game.entity.object.Direction;
import com.github.steveice10.mc.protocol.data.game.level.particle.Particle;
import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import net.kyori.adventure.text.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class MetadataType<T> {
    private static final List<MetadataType<?>> VALUES = new ArrayList<>();

    public static final ByteMetadataType BYTE = new ByteMetadataType(NetInput::readByte, NetOutput::writeByte, ByteEntityMetadata::new);
    public static final IntMetadataType INT = new IntMetadataType(NetInput::readVarInt, NetOutput::writeVarInt, IntEntityMetadata::new);
    public static final FloatMetadataType FLOAT = new FloatMetadataType(NetInput::readFloat, NetOutput::writeFloat, FloatEntityMetadata::new);
    public static final MetadataType<String> STRING = new MetadataType<>(NetInput::readString, NetOutput::writeString, ObjectEntityMetadata::new);
    public static final MetadataType<Component> CHAT = new MetadataType<>(in -> DefaultComponentSerializer.get().deserialize(in.readString()),
            (out, value) -> out.writeString(DefaultComponentSerializer.get().serialize(value)),
            ObjectEntityMetadata::new);
    public static final MetadataType<Optional<Component>> OPTIONAL_CHAT = new MetadataType<>(optionalReader(in -> DefaultComponentSerializer.get().deserialize(in.readString())),
            optionalWriter((out, value) -> out.writeString(DefaultComponentSerializer.get().serialize(value))),
            ObjectEntityMetadata::new);
    public static final MetadataType<ItemStack> ITEM = new MetadataType<>(ItemStack::read, ItemStack::write, ObjectEntityMetadata::new);
    public static final BooleanMetadataType BOOLEAN = new BooleanMetadataType(NetInput::readBoolean, NetOutput::writeBoolean, BooleanEntityMetadata::new);
    public static final MetadataType<Rotation> ROTATION = new MetadataType<>(Rotation::read, Rotation::write, ObjectEntityMetadata::new);
    public static final MetadataType<Position> POSITION = new MetadataType<>(Position::read, Position::write, ObjectEntityMetadata::new);
    public static final MetadataType<Optional<Position>> OPTIONAL_POSITION = new MetadataType<>(optionalReader(Position::read), optionalWriter(Position::write), ObjectEntityMetadata::new);
    public static final MetadataType<Direction> DIRECTION = new MetadataType<>(in -> in.readEnum(Direction.VALUES), NetOutput::writeEnum, ObjectEntityMetadata::new);
    public static final MetadataType<Optional<UUID>> OPTIONAL_UUID = new MetadataType<>(optionalReader(NetInput::readUUID), optionalWriter(NetOutput::writeUUID), ObjectEntityMetadata::new);
    public static final IntMetadataType BLOCK_STATE = new IntMetadataType(NetInput::readVarInt, NetOutput::writeVarInt, IntEntityMetadata::new);
    public static final MetadataType<CompoundTag> NBT_TAG = new MetadataType<>(NBT::read, NBT::write, ObjectEntityMetadata::new);
    public static final MetadataType<Particle> PARTICLE = new MetadataType<>(Particle::read, Particle::write, ObjectEntityMetadata::new);
    public static final MetadataType<VillagerData> VILLAGER_DATA = new MetadataType<>(VillagerData::read, VillagerData::write, ObjectEntityMetadata::new);
    public static final OptionalIntMetadataType OPTIONAL_VARINT = new OptionalIntMetadataType(ObjectEntityMetadata::new);
    public static final MetadataType<Pose> POSE = new MetadataType<>(in -> in.readEnum(Pose.VALUES), NetOutput::writeEnum, ObjectEntityMetadata::new);

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

    public EntityMetadata<T, ? extends MetadataType<T>> readMetadata(NetInput input, int id) throws IOException {
        return this.metadataFactory.create(id, this, this.reader.read(input));
    }

    public void writeMetadata(NetOutput output, T value) throws IOException {
        this.writer.write(output, value);
    }

    @FunctionalInterface
    public interface Reader<V> {
        V read(NetInput input) throws IOException;
    }

    @FunctionalInterface
    public interface Writer<V> {
        void write(NetOutput output, V value) throws IOException;
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
        return (ouput, value) -> {
              ouput.writeBoolean(value.isPresent());
              if (value.isPresent()) {
                  writer.write(ouput, value.get());
              }
        };
    }

    public static MetadataType<?> read(NetInput in) throws IOException {
        int id = in.readVarInt();
        if (id >= VALUES.size()) {
            throw new IllegalArgumentException("Received id " + id + " for MetadataType when the maximum was " + VALUES.size() + "!");
        }

        return VALUES.get(id);
    }

    public static void write(NetOutput out, MetadataType<?> type) throws IOException {
        out.writeVarInt(type.id);
    }
}
