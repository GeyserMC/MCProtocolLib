package com.github.steveice10.mc.protocol.packet.ingame.server.world;

import com.github.steveice10.mc.protocol.data.game.NBT;
import com.github.steveice10.mc.protocol.data.game.chunk.Chunk;
import com.github.steveice10.mc.protocol.data.game.chunk.Column;
import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.io.stream.StreamNetInput;
import com.github.steveice10.packetlib.io.stream.StreamNetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.With;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.BitSet;

@Data
@With
@Setter(AccessLevel.NONE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ServerChunkDataPacket implements Packet {
    private @NonNull Column column;

    @Override
    public void read(NetInput in) throws IOException {
        int x = in.readInt();
        int z = in.readInt();
        BitSet chunkMask = BitSet.valueOf(in.readLongs(in.readVarInt()));
        CompoundTag heightMaps = NBT.read(in);
        int[] biomeData = new int[in.readVarInt()];
        for (int index = 0; index < biomeData.length; index++) {
            biomeData[index] = in.readVarInt();
        }
        byte[] data = in.readBytes(in.readVarInt());

        NetInput dataIn = new StreamNetInput(new ByteArrayInputStream(data));
        Chunk[] chunks = new Chunk[chunkMask.size()];
        for (int index = 0; index < chunks.length; index++) {
            if (chunkMask.get(index)) {
                chunks[index] = Chunk.read(dataIn);
            }
        }

        CompoundTag[] tileEntities = new CompoundTag[in.readVarInt()];
        for (int i = 0; i < tileEntities.length; i++) {
            tileEntities[i] = NBT.read(in);
        }

        this.column = new Column(x, z, chunks, tileEntities, heightMaps, biomeData);
    }

    @Override
    public void write(NetOutput out) throws IOException {
        ByteArrayOutputStream dataBytes = new ByteArrayOutputStream();
        NetOutput dataOut = new StreamNetOutput(dataBytes);

        BitSet bitSet = new BitSet();
        Chunk[] chunks = this.column.getChunks();
        for (int index = 0; index < chunks.length; index++) {
            Chunk chunk = chunks[index];
            if (chunk != null && !chunk.isEmpty()) {
                bitSet.set(index);
                Chunk.write(dataOut, chunk);
            }
        }

        out.writeInt(this.column.getX());
        out.writeInt(this.column.getZ());
        long[] longArray = bitSet.toLongArray();
        out.writeVarInt(longArray.length);
        for (long content : longArray) {
            out.writeLong(content);
        }
        NBT.write(out, this.column.getHeightMaps());
        out.writeVarInt(this.column.getBiomeData().length);
        for (int biomeData : this.column.getBiomeData()) {
            out.writeVarInt(biomeData);
        }
        out.writeVarInt(dataBytes.size());
        out.writeBytes(dataBytes.toByteArray(), dataBytes.size());
        out.writeVarInt(this.column.getTileEntities().length);
        for (CompoundTag tag : this.column.getTileEntities()) {
            NBT.write(out, tag);
        }
    }

    @Override
    public boolean isPriority() {
        return false;
    }
}
