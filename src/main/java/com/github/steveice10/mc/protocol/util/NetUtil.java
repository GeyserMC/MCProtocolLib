package com.github.steveice10.mc.protocol.util;

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.EntityMetadata;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.MetadataType;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.Pose;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.Rotation;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.VillagerData;
import com.github.steveice10.mc.protocol.data.game.world.block.BlockFace;
import com.github.steveice10.mc.protocol.data.game.world.block.BlockState;
import com.github.steveice10.mc.protocol.data.game.world.particle.BlockParticleData;
import com.github.steveice10.mc.protocol.data.game.world.particle.DustParticleData;
import com.github.steveice10.mc.protocol.data.game.world.particle.FallingDustParticleData;
import com.github.steveice10.mc.protocol.data.game.world.particle.ItemParticleData;
import com.github.steveice10.mc.protocol.data.game.world.particle.Particle;
import com.github.steveice10.mc.protocol.data.game.world.particle.ParticleData;
import com.github.steveice10.mc.protocol.data.game.world.particle.ParticleType;
import com.github.steveice10.mc.protocol.data.message.Message;
import com.github.steveice10.opennbt.NBTIO;
import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;
import java.util.UUID;

public class NetUtil {
    private static final int POSITION_X_SIZE = 38;
    private static final int POSITION_Y_SIZE = 12;
    private static final int POSITION_Z_SIZE = 38;
    private static final int POSITION_Y_SHIFT = 0xFFF;
    private static final int POSITION_WRITE_SHIFT = 0x3FFFFFF;

    private NetUtil() {
    }

    public static CompoundTag readNBT(NetInput in) throws IOException {
        byte b = in.readByte();
        if(b == 0) {
            return null;
        } else {
            return (CompoundTag) NBTIO.readTag(new NetInputStream(in, b));
        }
    }

    public static void writeNBT(NetOutput out, CompoundTag tag) throws IOException {
        if(tag == null) {
            out.writeByte(0);
        } else {
            NBTIO.writeTag(new NetOutputStream(out), tag);
        }
    }

    public static BlockState readBlockState(NetInput in) throws IOException {
        return new BlockState(in.readVarInt());
    }

    public static void writeBlockState(NetOutput out, BlockState blockState) throws IOException {
        out.writeVarInt(blockState.getId());
    }

    public static ItemStack readItem(NetInput in) throws IOException {
        boolean present = in.readBoolean();
        if(!present) {
            return null;
        }

        int item = in.readVarInt();
        return new ItemStack(item, in.readByte(), readNBT(in));
    }

    public static void writeItem(NetOutput out, ItemStack item) throws IOException {
        out.writeBoolean(item != null);
        if (item != null) {
            out.writeVarInt(item.getId());
            out.writeByte(item.getAmount());
            writeNBT(out, item.getNbt());
        }
    }

    public static Position readPosition(NetInput in) throws IOException {
        long val = in.readLong();

        int x = (int) (val >> POSITION_X_SIZE);
        int y = (int) (val  & POSITION_Y_SHIFT);
        int z = (int) (val << 26 >> POSITION_Z_SIZE);

        return new Position(x, y, z);
    }

    public static void writePosition(NetOutput out, Position pos) throws IOException {
        long x = pos.getX() & POSITION_WRITE_SHIFT;
        long y = pos.getY() & POSITION_Y_SHIFT;
        long z = pos.getZ() & POSITION_WRITE_SHIFT;

        out.writeLong(x << POSITION_X_SIZE | z << POSITION_Y_SIZE | y);
    }

    public static Rotation readRotation(NetInput in) throws IOException {
        return new Rotation(in.readFloat(), in.readFloat(), in.readFloat());
    }

    public static void writeRotation(NetOutput out, Rotation rot) throws IOException {
        out.writeFloat(rot.getPitch());
        out.writeFloat(rot.getYaw());
        out.writeFloat(rot.getRoll());
    }

    public static Particle readParticle(NetInput in) throws IOException {
        ParticleType type = MagicValues.key(ParticleType.class, in.readVarInt());
        ParticleData data = readParticleData(in, type);
        return new Particle(type, data);
    }

    public static void writeParticle(NetOutput out, Particle particle) throws IOException {
        out.writeVarInt(MagicValues.value(Integer.class, particle.getType()));
        writeParticleData(out, particle.getData(), particle.getType());
    }

    public static ParticleData readParticleData(NetInput in, ParticleType type) throws IOException {
        switch (type) {
            case BLOCK:
                return new BlockParticleData(readBlockState(in));
            case DUST:
                float red = in.readFloat();
                float green = in.readFloat();
                float blue = in.readFloat();
                float scale = in.readFloat();
                return new DustParticleData(red, green, blue, scale);
            case FALLING_DUST:
                return new FallingDustParticleData(readBlockState(in));
            case ITEM:
                return new ItemParticleData(readItem(in));
            default:
                return null;
        }
    }

    public static void writeParticleData(NetOutput out, ParticleData data, ParticleType type) throws IOException {
        switch (type) {
            case BLOCK:
                writeBlockState(out, ((BlockParticleData) data).getBlockState());
                break;
            case DUST:
                out.writeFloat(((DustParticleData) data).getRed());
                out.writeFloat(((DustParticleData) data).getGreen());
                out.writeFloat(((DustParticleData) data).getBlue());
                out.writeFloat(((DustParticleData) data).getScale());
                break;
            case FALLING_DUST:
                writeBlockState(out, ((FallingDustParticleData) data).getBlockState());
                break;
            case ITEM:
                writeItem(out, ((ItemParticleData) data).getItemStack());
                break;
        }
    }

    public static EntityMetadata[] readEntityMetadata(NetInput in) throws IOException {
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
                    if (!chatPresent) {
                        break;
                    }
                    // Intentional fall-through
                case CHAT:
                    value = Message.fromString(in.readString());
                    break;
                case ITEM:
                    value = readItem(in);
                    break;
                case BOOLEAN:
                    value = in.readBoolean();
                    break;
                case ROTATION:
                    value = readRotation(in);
                    break;
                case POSITION:
                    value = readPosition(in);
                    break;
                case OPTIONAL_POSITION:
                    boolean positionPresent = in.readBoolean();
                    if(positionPresent) {
                        value = readPosition(in);
                    }

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
                case BLOCK_STATE:
                    value = readBlockState(in);
                    break;
                case NBT_TAG:
                    value = readNBT(in);
                    break;
                case PARTICLE:
                    value = readParticle(in);
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

    public static void writeEntityMetadata(NetOutput out, EntityMetadata[] metadata) throws IOException {
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
                    if (meta.getValue() == null) {
                        break;
                    }
                    // Intentional fall-through
                case CHAT:
                    out.writeString(((Message) meta.getValue()).toJsonString());
                    break;
                case ITEM:
                    writeItem(out, (ItemStack) meta.getValue());
                    break;
                case BOOLEAN:
                    out.writeBoolean((Boolean) meta.getValue());
                    break;
                case ROTATION:
                    writeRotation(out, (Rotation) meta.getValue());
                    break;
                case POSITION:
                    writePosition(out, (Position) meta.getValue());
                    break;
                case OPTIONAL_POSITION:
                    out.writeBoolean(meta.getValue() != null);
                    if(meta.getValue() != null) {
                        writePosition(out, (Position) meta.getValue());
                    }

                    break;
                case BLOCK_FACE:
                    out.writeVarInt(MagicValues.value(Integer.class, (BlockFace) meta.getValue()));
                    break;
                case OPTIONAL_UUID:
                    out.writeBoolean(meta.getValue() != null);
                    if(meta.getValue() != null) {
                        out.writeUUID((UUID) meta.getValue());
                    }

                    break;
                case BLOCK_STATE:
                    writeBlockState(out, (BlockState) meta.getValue());
                    break;
                case NBT_TAG:
                    writeNBT(out, (CompoundTag) meta.getValue());
                    break;
                case PARTICLE:
                    writeParticle(out, (Particle) meta.getValue());
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

    private static class NetInputStream extends InputStream {
        private NetInput in;
        private boolean readFirst;
        private byte firstByte;

        public NetInputStream(NetInput in, byte firstByte) {
            this.in = in;
            this.firstByte = firstByte;
        }

        @Override
        public int read() throws IOException {
            if(!this.readFirst) {
                this.readFirst = true;
                return this.firstByte;
            } else {
                return this.in.readUnsignedByte();
            }
        }
    }

    private static class NetOutputStream extends OutputStream {
        private NetOutput out;

        public NetOutputStream(NetOutput out) {
            this.out = out;
        }

        @Override
        public void write(int b) throws IOException {
            this.out.writeByte(b);
        }
    }
}
