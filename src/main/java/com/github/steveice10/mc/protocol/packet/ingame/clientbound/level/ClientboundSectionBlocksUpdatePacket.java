package com.github.steveice10.mc.protocol.packet.ingame.clientbound.level;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import com.github.steveice10.mc.protocol.data.game.level.block.BlockChangeEntry;
import org.cloudburstmc.math.vector.Vector3i;
import io.netty.buffer.ByteBuf;
import lombok.Data;
import lombok.NonNull;
import lombok.With;

import java.io.IOException;

@Data
@With
public class ClientboundSectionBlocksUpdatePacket implements MinecraftPacket {
    private final int chunkX;
    private final int chunkY;
    private final int chunkZ;
    private final boolean ignoreOldLight;
    /**
     * The server sends the record position in terms of the local chunk coordinate but it is stored here in terms of global coordinates.
     */
    private final @NonNull BlockChangeEntry[] entries;

    public ClientboundSectionBlocksUpdatePacket(int chunkX, int chunkY, int chunkZ, boolean ignoreOldLight, BlockChangeEntry... entries) {
        if (entries == null || entries.length == 0) {
            throw new IllegalArgumentException("Entries must contain at least 1 value.");
        }

        this.chunkX = chunkX;
        this.chunkY = chunkY;
        this.chunkZ = chunkZ;
        this.ignoreOldLight = ignoreOldLight;
        this.entries = entries;
    }

    public ClientboundSectionBlocksUpdatePacket(ByteBuf in, MinecraftCodecHelper helper) throws IOException {
        long chunkPosition = in.readLong();
        this.chunkX = (int) (chunkPosition >> 42);
        this.chunkY = (int) (chunkPosition << 44 >> 44);
        this.chunkZ = (int) (chunkPosition << 22 >> 42);
        this.ignoreOldLight = in.readBoolean();
        this.entries = new BlockChangeEntry[helper.readVarInt(in)];
        for (int index = 0; index < this.entries.length; index++) {
            long blockData = helper.readVarLong(in);
            short position = (short) (blockData & 0xFFFL);
            int x = (this.chunkX << 4) + (position >>> 8 & 0xF);
            int y = (this.chunkY << 4) + (position & 0xF);
            int z = (this.chunkZ << 4) + (position >>> 4 & 0xF);
            this.entries[index] = new BlockChangeEntry(Vector3i.from(x, y, z), (int) (blockData >>> 12));
        }
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
        long chunkPosition = 0;
        chunkPosition |= (this.chunkX & 0x3FFFFFL) << 42;
        chunkPosition |= (this.chunkZ & 0x3FFFFFL) << 20;
        out.writeLong(chunkPosition | (this.chunkY & 0xFFFFFL));
        out.writeBoolean(this.ignoreOldLight);
        helper.writeVarInt(out, this.entries.length);
        for (BlockChangeEntry entry : this.entries) {
            short position = (short) ((entry.getPosition().getX() - (this.chunkX << 4)) << 8 | (entry.getPosition().getZ() - (this.chunkZ << 4)) << 4 | (entry.getPosition().getY() - (this.chunkY << 4)));
            helper.writeVarLong(out, (long) entry.getBlock() << 12 | position);
        }
    }
}
