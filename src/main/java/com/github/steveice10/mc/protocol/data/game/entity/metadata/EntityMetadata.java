package com.github.steveice10.mc.protocol.data.game.entity.metadata;

import com.github.steveice10.mc.protocol.data.DefaultComponentSerializer;
import com.github.steveice10.mc.protocol.data.game.NBT;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.type.*;
import com.github.steveice10.mc.protocol.data.game.entity.object.Direction;
import com.github.steveice10.mc.protocol.data.game.level.particle.Particle;
import com.github.steveice10.mc.protocol.data.game.level.particle.ParticleData;
import com.github.steveice10.mc.protocol.data.game.level.particle.ParticleType;
import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import net.kyori.adventure.text.Component;

import java.io.IOException;
import java.util.*;

@Data
@AllArgsConstructor
public abstract class EntityMetadata<T> {
    private final int id;
    private final @NonNull MetadataType type;

    /**
     * May be null depending on type
     */
    public abstract T getValue();

    public static EntityMetadata<?>[] read(NetInput in) throws IOException {
        List<EntityMetadata<?>> ret = new ArrayList<>();
        int id;
        while ((id = in.readUnsignedByte()) != 255) {
            MetadataType type = in.readEnum(MetadataType.VALUES);
            EntityMetadata<?> metadata;
            switch (type) {
                case BYTE:
                    metadata = new ByteEntityMetadata(id, in.readByte());
                    break;
                case INT:
                case BLOCK_STATE:
                    metadata = new IntEntityMetadata(id, type, in.readVarInt());
                    break;
                case FLOAT:
                    metadata = new FloatEntityMetadata(id, in.readFloat());
                    break;
                case STRING:
                    metadata = new ObjectEntityMetadata<>(id, MetadataType.STRING, in.readString());
                    break;
                case OPTIONAL_CHAT:
                    boolean chatPresent = in.readBoolean();
                    if (!chatPresent) {
                        metadata = new ObjectEntityMetadata<Component>(id, MetadataType.OPTIONAL_CHAT, null);
                        break;
                    }

                    // Intentional fall-through
                case CHAT:
                    Component value = DefaultComponentSerializer.get().deserialize(in.readString());
                    metadata = new ObjectEntityMetadata<>(id, type, value);
                    break;
                case ITEM:
                    metadata = new ObjectEntityMetadata<>(id, MetadataType.ITEM, ItemStack.read(in));
                    break;
                case BOOLEAN:
                    metadata = new BooleanEntityMetadata(id, in.readBoolean());
                    break;
                case ROTATION:
                    metadata = new ObjectEntityMetadata<>(id, MetadataType.ROTATION, Rotation.read(in));
                    break;
                case OPTIONAL_POSITION:
                    boolean positionPresent = in.readBoolean();
                    if (!positionPresent) {
                        metadata = new ObjectEntityMetadata<Position>(id, MetadataType.OPTIONAL_POSITION, null);
                        break;
                    }

                    // Intentional fall-through
                case POSITION:
                    metadata = new ObjectEntityMetadata<>(id, type, Position.read(in));
                    break;
                case DIRECTION:
                    metadata = new ObjectEntityMetadata<>(id, MetadataType.DIRECTION, in.readEnum(Direction.VALUES));
                    break;
                case OPTIONAL_UUID:
                    boolean uuidPresent = in.readBoolean();
                    if (uuidPresent) {
                        metadata = new ObjectEntityMetadata<>(id, MetadataType.OPTIONAL_UUID, in.readUUID());
                    } else {
                        metadata = new ObjectEntityMetadata<UUID>(id, MetadataType.OPTIONAL_UUID, null);
                    }
                    break;
                case NBT_TAG:
                    metadata = new ObjectEntityMetadata<>(id, MetadataType.NBT_TAG, NBT.read(in));
                    break;
                case PARTICLE:
                    ParticleType particleType = in.readEnum(ParticleType.VALUES);
                    Particle particle = new Particle(particleType, ParticleData.read(in, particleType));
                    metadata = new ObjectEntityMetadata<>(id, MetadataType.PARTICLE, particle);
                    break;
                case VILLAGER_DATA:
                    VillagerData villagerData = new VillagerData(in.readVarInt(), in.readVarInt(), in.readVarInt());
                    metadata = new ObjectEntityMetadata<>(id, MetadataType.VILLAGER_DATA, villagerData);
                    break;
                case OPTIONAL_VARINT:
                    int i = in.readVarInt();
                    metadata = new ObjectEntityMetadata<>(id, MetadataType.OPTIONAL_VARINT, i == 0 ? OptionalInt.empty() : OptionalInt.of(i - 1));
                    break;
                case POSE:
                    metadata = new ObjectEntityMetadata<>(id, MetadataType.POSE, in.readEnum(Pose.VALUES));
                    break;
                default:
                    throw new IOException("Unknown metadata type id: " + type);
            }

            ret.add(metadata);
        }

        return ret.toArray(new EntityMetadata[0]);
    }

    /**
     * Overridden for primitive classes. This write method still checks for these primitives in the event
     * they are manually created using {@link ObjectEntityMetadata}.
     */
    protected void write(NetOutput out) throws IOException {
        switch (getType()) {
            case BYTE:
                out.writeByte((Byte) getValue());
                break;
            case INT:
            case BLOCK_STATE:
                out.writeVarInt((Integer) getValue());
                break;
            case FLOAT:
                out.writeFloat((Float) getValue());
                break;
            case STRING:
                out.writeString((String) getValue());
                break;
            case OPTIONAL_CHAT:
                out.writeBoolean(getValue() != null);
                if (getValue() == null) {
                    break;
                }

                // Intentional fall-through
            case CHAT:
                out.writeString(DefaultComponentSerializer.get().serialize((Component) getValue()));
                break;
            case ITEM:
                ItemStack.write(out, (ItemStack) getValue());
                break;
            case BOOLEAN:
                out.writeBoolean((Boolean) getValue());
                break;
            case ROTATION:
                Rotation.write(out, (Rotation) getValue());
                break;
            case OPTIONAL_POSITION:
                out.writeBoolean(getValue() != null);
                if (getValue() == null) {
                    break;
                }

                // Intentional fall-through
            case POSITION:
                Position.write(out, (Position) getValue());
                break;
            case DIRECTION:
                out.writeEnum((Direction) getValue());
                break;
            case OPTIONAL_UUID:
                out.writeBoolean(getValue() != null);
                if (getValue() != null) {
                    out.writeUUID((UUID) getValue());
                }

                break;
            case NBT_TAG:
                NBT.write(out, (CompoundTag) getValue());
                break;
            case PARTICLE:
                Particle particle = (Particle) getValue();
                out.writeEnum(particle.getType());
                ParticleData.write(out, particle.getType(), particle.getData());
                break;
            case VILLAGER_DATA:
                VillagerData villagerData = (VillagerData) getValue();
                out.writeVarInt(villagerData.getType());
                out.writeVarInt(villagerData.getProfession());
                out.writeVarInt(villagerData.getLevel());
                break;
            case OPTIONAL_VARINT:
                OptionalInt optionalInt = (OptionalInt) getValue();
                out.writeVarInt(optionalInt.orElse(-1) + 1);
                break;
            case POSE:
                out.writeEnum((Pose) getValue());
                break;
            default:
                throw new IOException("Unknown metadata type: " + getType());
        }
    }

    public static void write(NetOutput out, EntityMetadata<?>[] metadata) throws IOException {
        for (EntityMetadata<?> meta : metadata) {
            out.writeByte(meta.getId());
            out.writeEnum(meta.getType());
            meta.write(out);
        }

        out.writeByte(255);
    }

    @Override
    public String toString() {
        return "EntityMetadata(id=" + id + ", type=" + type + ", value=" + getValue().toString() + ")";
    }
}
