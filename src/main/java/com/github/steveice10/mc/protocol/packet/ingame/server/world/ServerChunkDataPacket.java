package com.github.steveice10.mc.protocol.packet.ingame.server.world;

import com.github.steveice10.mc.protocol.data.game.NBT;
import com.github.steveice10.mc.protocol.data.game.chunk.Chunk;
import com.github.steveice10.mc.protocol.data.game.chunk.Column;
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
import net.kyori.adventure.nbt.CompoundBinaryTag;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Data
@Setter(AccessLevel.NONE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ServerChunkDataPacket implements Packet {
    private @NonNull Column column;

    @Override
    public void read(NetInput in) throws IOException {
        int x = in.readInt();
        int z = in.readInt();
        boolean fullChunk = in.readBoolean();
        int chunkMask = in.readVarInt();
        CompoundBinaryTag heightMaps = NBT.read(in);
        int[] biomeData = fullChunk ? new int[in.readVarInt()] : null;
        if (fullChunk) {
            for (int index = 0; index < biomeData.length; index++) {
                biomeData[index] = in.readVarInt();
            }
        }
        byte[] data = in.readBytes(in.readVarInt());

        NetInput dataIn = new StreamNetInput(new ByteArrayInputStream(data));
        Chunk[] chunks = new Chunk[16];
        for(int index = 0; index < chunks.length; index++) {
            if((chunkMask & (1 << index)) != 0) {
                chunks[index] = Chunk.read(dataIn);
            }
        }

        CompoundBinaryTag[] tileEntities = new CompoundBinaryTag[in.readVarInt()];
        for(int i = 0; i < tileEntities.length; i++) {
            tileEntities[i] = NBT.read(in);
        }

        this.column = new Column(x, z, chunks, tileEntities, heightMaps, biomeData);
    }

    @Override
    public void write(NetOutput out) throws IOException {
        ByteArrayOutputStream dataBytes = new ByteArrayOutputStream();
        NetOutput dataOut = new StreamNetOutput(dataBytes);

        int mask = 0;
        Chunk[] chunks = this.column.getChunks();
        for(int index = 0; index < chunks.length; index++) {
            Chunk chunk = chunks[index];
            if(chunk != null && (this.column.getBiomeData() == null || !chunk.isEmpty())) {
                mask |= 1 << index;
                Chunk.write(dataOut, chunk);
            }
        }

        boolean fullChunk = this.column.getBiomeData() != null;

        out.writeInt(this.column.getX());
        out.writeInt(this.column.getZ());
        out.writeBoolean(fullChunk);
        out.writeVarInt(mask);
        NBT.write(out, this.column.getHeightMaps());
        if (fullChunk) {
            out.writeVarInt(this.column.getBiomeData().length);
            for (int biomeData : this.column.getBiomeData()) {
                out.writeVarInt(biomeData);
            }
        }
        out.writeVarInt(dataBytes.size());
        out.writeBytes(dataBytes.toByteArray(), dataBytes.size());
        out.writeVarInt(this.column.getTileEntities().length);
        for(CompoundBinaryTag tag : this.column.getTileEntities()) {
            NBT.write(out, tag);
        }
    }

    @Override
    public boolean isPriority() {
        return false;
    }
}
