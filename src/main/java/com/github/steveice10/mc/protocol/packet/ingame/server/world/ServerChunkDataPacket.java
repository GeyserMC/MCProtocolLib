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
        CompoundTag heightMaps = NBT.read(in);
        byte[] data = in.readBytes(in.readVarInt());
        CompoundTag[] tileEntities = new CompoundTag[in.readVarInt()];
        for(int i = 0; i < tileEntities.length; i++) {
            tileEntities[i] = NBT.read(in);
        }

        NetInput dataIn = new StreamNetInput(new ByteArrayInputStream(data));
        Chunk[] chunks = new Chunk[16];
        for(int index = 0; index < chunks.length; index++) {
            if((chunkMask & (1 << index)) != 0) {
                chunks[index] = Chunk.read(dataIn);
            }
        }

        int[] biomeData = null;
        if(fullChunk) {
            biomeData = dataIn.readInts(256);
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

        if(this.column.getBiomeData() != null) {
            dataOut.writeInts(this.column.getBiomeData());
        }

        out.writeInt(this.column.getX());
        out.writeInt(this.column.getZ());
        out.writeBoolean(this.column.getBiomeData() != null);
        out.writeVarInt(mask);
        NBT.write(out, this.column.getHeightMaps());
        out.writeVarInt(dataBytes.size());
        out.writeBytes(dataBytes.toByteArray(), dataBytes.size());
        out.writeVarInt(this.column.getTileEntities().length);
        for(CompoundTag tag : this.column.getTileEntities()) {
            NBT.write(out, tag);
        }
    }

    @Override
    public boolean isPriority() {
        return false;
    }
}
