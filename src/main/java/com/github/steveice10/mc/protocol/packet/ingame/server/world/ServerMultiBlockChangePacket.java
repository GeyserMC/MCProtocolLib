package com.github.steveice10.mc.protocol.packet.ingame.server.world;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
import com.github.steveice10.mc.protocol.data.game.world.block.BlockChangeRecord;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.With;

import java.io.IOException;

@Data
@With
@Setter(AccessLevel.NONE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ServerMultiBlockChangePacket implements Packet {
    private int chunkX;
    private int chunkY;
    private int chunkZ;
    private boolean ignoreOldLight;
    /**
     * The server sends the record position in terms of the local chunk coordinate but it is stored here in terms of global coordinates.
     */
    private @NonNull BlockChangeRecord[] records;

    public ServerMultiBlockChangePacket(int chunkX, int chunkY, int chunkZ, boolean ignoreOldLight, BlockChangeRecord... records) {
        if (records == null || records.length == 0) {
            throw new IllegalArgumentException("Records must contain at least 1 value.");
        }

        this.chunkX = chunkX;
        this.chunkY = chunkY;
        this.chunkZ = chunkZ;
        this.ignoreOldLight = ignoreOldLight;
        this.records = records;
    }

    @Override
    public void read(NetInput in) throws IOException {
        long chunkPosition = in.readLong();
        this.chunkX = (int) (chunkPosition >> 42);
        this.chunkY = (int) (chunkPosition << 44 >> 44);
        this.chunkZ = (int) (chunkPosition << 22 >> 42);
        this.ignoreOldLight = in.readBoolean();
        this.records = new BlockChangeRecord[in.readVarInt()];
        for (int index = 0; index < this.records.length; index++) {
            long blockData = in.readVarLong();
            short position = (short) (blockData & 0xFFFL);
            int x = (this.chunkX << 4) + (position >>> 8 & 0xF);
            int y = (this.chunkY << 4) + (position & 0xF);
            int z = (this.chunkZ << 4) + (position >>> 4 & 0xF);
            this.records[index] = new BlockChangeRecord(new Position(x, y, z), (int) (blockData >>> 12));
        }
    }

    @Override
    public void write(NetOutput out) throws IOException {
        long chunkPosition = 0;
        chunkPosition |= (this.chunkX & 0x3FFFFFL) << 42;
        chunkPosition |= (this.chunkZ & 0x3FFFFFL) << 20;
        out.writeLong(chunkPosition | (this.chunkY & 0xFFFFFL));
        out.writeBoolean(this.ignoreOldLight);
        out.writeVarInt(this.records.length);
        for (BlockChangeRecord record : this.records) {
            short position = (short) ((record.getPosition().getX() - (this.chunkX << 4)) << 8 | (record.getPosition().getZ() - (this.chunkZ << 4)) << 4 | (record.getPosition().getY() - (this.chunkY << 4)));
            out.writeVarLong((long) record.getBlock() << 12 | position);
        }
    }

    @Override
    public boolean isPriority() {
        return false;
    }
}
