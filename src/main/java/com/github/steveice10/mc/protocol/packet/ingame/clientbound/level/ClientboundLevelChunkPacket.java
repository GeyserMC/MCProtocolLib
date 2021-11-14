package com.github.steveice10.mc.protocol.packet.ingame.clientbound.level;

import com.github.steveice10.mc.protocol.data.game.NBT;
import com.github.steveice10.mc.protocol.data.game.chunk.ChunkSection;
import com.github.steveice10.mc.protocol.data.game.chunk.Chunk;
import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.io.stream.StreamNetInput;
import com.github.steveice10.packetlib.io.stream.StreamNetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.BitSet;

@Data
@With
@AllArgsConstructor
public class ClientboundLevelChunkPacket implements Packet {
    private final @NonNull Chunk chunk;

    public ClientboundLevelChunkPacket(NetInput in) throws IOException {
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
        ChunkSection[] sections = new ChunkSection[chunkMask.size()];
        for (int index = 0; index < sections.length; index++) {
            if (chunkMask.get(index)) {
                sections[index] = ChunkSection.read(dataIn);
            }
        }

        CompoundTag[] blockEntities = new CompoundTag[in.readVarInt()];
        for (int i = 0; i < blockEntities.length; i++) {
            blockEntities[i] = NBT.read(in);
        }

        this.chunk = new Chunk(x, z, sections, blockEntities, heightMaps, biomeData);
    }

    @Override
    public void write(NetOutput out) throws IOException {
        ByteArrayOutputStream dataBytes = new ByteArrayOutputStream();
        NetOutput dataOut = new StreamNetOutput(dataBytes);

        BitSet bitSet = new BitSet();
        ChunkSection[] sections = this.chunk.getSections();
        for (int index = 0; index < sections.length; index++) {
            ChunkSection section = sections[index];
            if (section != null && !section.isEmpty()) {
                bitSet.set(index);
                ChunkSection.write(dataOut, section);
            }
        }

        out.writeInt(this.chunk.getX());
        out.writeInt(this.chunk.getZ());
        long[] longArray = bitSet.toLongArray();
        out.writeVarInt(longArray.length);
        for (long content : longArray) {
            out.writeLong(content);
        }
        NBT.write(out, this.chunk.getHeightMaps());
        out.writeVarInt(this.chunk.getBiomeData().length);
        for (int biomeData : this.chunk.getBiomeData()) {
            out.writeVarInt(biomeData);
        }
        out.writeVarInt(dataBytes.size());
        out.writeBytes(dataBytes.toByteArray(), dataBytes.size());
        out.writeVarInt(this.chunk.getBlockEntities().length);
        for (CompoundTag tag : this.chunk.getBlockEntities()) {
            NBT.write(out, tag);
        }
    }
}
