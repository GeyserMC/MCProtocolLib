package com.github.steveice10.mc.protocol.util;

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.chunk.BlockStorage;
import com.github.steveice10.mc.protocol.data.game.chunk.Chunk;
import com.github.steveice10.mc.protocol.data.game.chunk.Column;
import com.github.steveice10.mc.protocol.data.game.chunk.NibbleArray3d;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.EntityMetadata;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.MetadataType;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.Rotation;
import com.github.steveice10.mc.protocol.data.game.world.block.BlockFace;
import com.github.steveice10.mc.protocol.data.game.world.block.BlockState;
import com.github.steveice10.opennbt.NBTIO;
import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.io.stream.StreamNetInput;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NetUtil {
    private static final int POSITION_X_SIZE = 38;
    private static final int POSITION_Y_SIZE = 26;
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
        int rawId = in.readVarInt();
        return new BlockState(rawId >> 4, rawId & 0xF);
    }

    public static void writeBlockState(NetOutput out, BlockState blockState) throws IOException {
        out.writeVarInt((blockState.getId() << 4) | (blockState.getData() & 0xF));
    }

    public static ItemStack readItem(NetInput in) throws IOException {
        short item = in.readShort();
        if(item < 0) {
            return null;
        } else {
            return new ItemStack(item, in.readByte(), in.readShort(), readNBT(in));
        }
    }

    public static void writeItem(NetOutput out, ItemStack item) throws IOException {
        if(item == null) {
            out.writeShort(-1);
        } else {
            out.writeShort(item.getId());
            out.writeByte(item.getAmount());
            out.writeShort(item.getData());
            writeNBT(out, item.getNBT());
        }
    }

    public static Position readPosition(NetInput in) throws IOException {
        long val = in.readLong();

        int x = (int) (val >> POSITION_X_SIZE);
        int y = (int) ((val >> POSITION_Y_SIZE) & POSITION_Y_SHIFT);
        int z = (int) ((val << POSITION_Z_SIZE) >> POSITION_Z_SIZE);

        return new Position(x, y, z);
    }

    public static void writePosition(NetOutput out, Position pos) throws IOException {
        long x = pos.getX() & POSITION_WRITE_SHIFT;
        long y = pos.getY() & POSITION_Y_SHIFT;
        long z = pos.getZ() & POSITION_WRITE_SHIFT;

        out.writeLong(x << POSITION_X_SIZE | y << POSITION_Y_SIZE | z);
    }

    public static Rotation readRotation(NetInput in) throws IOException {
        return new Rotation(in.readFloat(), in.readFloat(), in.readFloat());
    }

    public static void writeRotation(NetOutput out, Rotation rot) throws IOException {
        out.writeFloat(rot.getPitch());
        out.writeFloat(rot.getYaw());
        out.writeFloat(rot.getRoll());
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
                case CHAT:
                    value = in.readString();
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
                case CHAT:
                    out.writeString((String) meta.getValue());
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
                default:
                    throw new IOException("Unknown metadata type: " + meta.getType());
            }
        }

        out.writeByte(255);
    }

    public static Column readColumn(byte data[], int x, int z, boolean fullChunk, boolean hasSkylight, int mask, CompoundTag[] tileEntities) throws IOException {
        NetInput in = new StreamNetInput(new ByteArrayInputStream(data));
        Throwable ex = null;
        Column column = null;
        try {
            Chunk[] chunks = new Chunk[16];
            for(int index = 0; index < chunks.length; index++) {
                if((mask & (1 << index)) != 0) {
                    BlockStorage blocks = new BlockStorage(in);
                    NibbleArray3d blocklight = new NibbleArray3d(in, 2048);
                    NibbleArray3d skylight = hasSkylight ? new NibbleArray3d(in, 2048) : null;
                    chunks[index] = new Chunk(blocks, blocklight, skylight);
                }
            }

            byte biomeData[] = null;
            if(fullChunk) {
                biomeData = in.readBytes(256);
            }

            column = new Column(x, z, chunks, biomeData, tileEntities);
        } catch(Throwable e) {
            ex = e;
        }

        // Unfortunately, this is needed to detect whether the chunks contain skylight or not.
        if((in.available() > 0 || ex != null) && !hasSkylight) {
            return readColumn(data, x, z, fullChunk, true, mask, tileEntities);
        } else if(ex != null) {
            throw new IOException("Failed to read chunk data.", ex);
        }

        return column;
    }

    public static int writeColumn(NetOutput out, Column column, boolean fullChunk, boolean hasSkylight) throws IOException {
        int mask = 0;
        Chunk chunks[] = column.getChunks();
        for(int index = 0; index < chunks.length; index++) {
            Chunk chunk = chunks[index];
            if(chunk != null && (!fullChunk || !chunk.isEmpty())) {
                mask |= 1 << index;
                chunk.getBlocks().write(out);
                chunk.getBlockLight().write(out);
                if(hasSkylight) {
                    chunk.getSkyLight().write(out);
                }
            }
        }

        if(fullChunk) {
            out.writeBytes(column.getBiomeData());
        }

        return mask;
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
