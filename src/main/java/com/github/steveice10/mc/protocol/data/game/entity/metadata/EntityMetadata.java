package com.github.steveice10.mc.protocol.data.game.entity.metadata;

import com.github.steveice10.mc.protocol.data.DefaultComponentSerializer;
import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.NBT;
import com.github.steveice10.mc.protocol.data.game.world.block.BlockFace;
import com.github.steveice10.mc.protocol.data.game.world.particle.Particle;
import com.github.steveice10.mc.protocol.data.game.world.particle.ParticleData;
import com.github.steveice10.mc.protocol.data.game.world.particle.ParticleType;
import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import net.kyori.adventure.text.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;
import java.util.UUID;

@Data
@AllArgsConstructor
public class EntityMetadata {
    private final int id;
    private final @NonNull MetadataType type;
    private final Object value;

    public static EntityMetadata[] read(NetInput in) throws IOException {
        List<EntityMetadata> ret = new ArrayList<EntityMetadata>();
        int id;
        while((id = in.readUnsignedByte()) != 255) {
            int typeId = in.readVarInt();
            MetadataType type = MagicValues.key(MetadataType.class, typeId);
            Object value = null;
            switch(type) {
                case BYTE:
                    value = in.readByte();
                    break;
                case INT:
                case BLOCK_STATE:
                    value = in.readVarInt();
                    break;
                case FLOAT:
                    value = in.readFloat();
                    break;
                case STRING:
                    value = in.readString();
                    break;
                case OPTIONAL_CHAT:
                    boolean chatPresent = in.readBoolean();
                    if(!chatPresent) {
                        break;
                    }

                    // Intentional fall-through
                case CHAT:
                    value = DefaultComponentSerializer.get().deserialize(in.readString());
                    break;
                case ITEM:
                    value = ItemStack.read(in);
                    break;
                case BOOLEAN:
                    value = in.readBoolean();
                    break;
                case ROTATION:
                    value = Rotation.read(in);
                    break;
                case OPTIONAL_POSITION:
                    boolean positionPresent = in.readBoolean();
                    if(!positionPresent) {
                        break;
                    }

                    // Intentional fall-through
                case POSITION:
                    value = Position.read(in);
                    break;
                case BLOCK_FACE:
                    value = MagicValues.key(BlockFace.class, in.readVarInt());
                    break;
                case OPTIONAL_UUID:
                    boolean uuidPresent = in.readBoolean();
                    if(uuidPresent) {
                        value = in.readUUID();
                    }

                    break;
                case NBT_TAG:
                    value = NBT.read(in);
                    break;
                case PARTICLE:
                    ParticleType particleType = ParticleType.values()[in.readVarInt()];
                    value = new Particle(particleType, ParticleData.read(in, particleType));
                    break;
                case VILLAGER_DATA:
                    value = new VillagerData(in.readVarInt(), in.readVarInt(), in.readVarInt());
                    break;
                case OPTIONAL_VARINT:
                    int i = in.readVarInt();
                    value = i == 0 ? OptionalInt.empty() : OptionalInt.of(i - 1);
                    break;
                case POSE:
                    value = MagicValues.key(Pose.class, in.readVarInt());
                    break;
                default:
                    throw new IOException("Unknown metadata type id: " + typeId);
            }

            ret.add(new EntityMetadata(id, type, value));
        }

        return ret.toArray(new EntityMetadata[ret.size()]);
    }

    public static void write(NetOutput out, EntityMetadata[] metadata) throws IOException {
        for(EntityMetadata meta : metadata) {
            out.writeByte(meta.getId());
            out.writeVarInt(MagicValues.value(Integer.class, meta.getType()));
            switch(meta.getType()) {
                case BYTE:
                    out.writeByte((Byte) meta.getValue());
                    break;
                case INT:
                    out.writeVarInt((Integer) meta.getValue());
                    break;
                case FLOAT:
                    out.writeFloat((Float) meta.getValue());
                    break;
                case STRING:
                    out.writeString((String) meta.getValue());
                    break;
                case OPTIONAL_CHAT:
                    out.writeBoolean(meta.getValue() != null);
                    if(meta.getValue() == null) {
                        break;
                    }

                    // Intentional fall-through
                case CHAT:
                    out.writeString(DefaultComponentSerializer.get().serialize((Component) meta.getValue()));
                    break;
                case ITEM:
                    ItemStack.write(out, (ItemStack) meta.getValue());
                    break;
                case BOOLEAN:
                    out.writeBoolean((Boolean) meta.getValue());
                    break;
                case ROTATION:
                    Rotation.write(out, (Rotation) meta.getValue());
                    break;
                case OPTIONAL_POSITION:
                    out.writeBoolean(meta.getValue() != null);
                    if(meta.getValue() == null) {
                        break;
                    }

                    // Intentional fall-through
                case POSITION:
                    Position.write(out, (Position) meta.getValue());
                    break;
                case BLOCK_FACE:
                    out.writeVarInt(MagicValues.value(Integer.class, meta.getValue()));
                    break;
                case OPTIONAL_UUID:
                    out.writeBoolean(meta.getValue() != null);
                    if(meta.getValue() != null) {
                        out.writeUUID((UUID) meta.getValue());
                    }

                    break;
                case BLOCK_STATE:
                    out.writeVarInt((int) meta.getValue());
                    break;
                case NBT_TAG:
                    NBT.write(out, (CompoundTag) meta.getValue());
                    break;
                case PARTICLE:
                    Particle particle = (Particle) meta.getValue();
                    out.writeVarInt(particle.getType().ordinal());
                    ParticleData.write(out, particle.getType(), particle.getData());
                    break;
                case VILLAGER_DATA:
                    VillagerData villagerData = (VillagerData) meta.getValue();
                    out.writeVarInt(villagerData.getType());
                    out.writeVarInt(villagerData.getProfession());
                    out.writeVarInt(villagerData.getLevel());
                    break;
                case OPTIONAL_VARINT:
                    OptionalInt optionalInt = (OptionalInt) meta.getValue();
                    out.writeVarInt(optionalInt.orElse(-1) + 1);
                    break;
                case POSE:
                    out.writeVarInt(MagicValues.value(Integer.class, meta.getValue()));
                    break;
                default:
                    throw new IOException("Unknown metadata type: " + meta.getType());
            }
        }

        out.writeByte(255);
    }
}
