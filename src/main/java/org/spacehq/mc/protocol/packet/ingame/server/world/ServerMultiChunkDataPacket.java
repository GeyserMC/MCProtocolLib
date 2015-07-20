package org.spacehq.mc.protocol.packet.ingame.server.world;

import org.spacehq.mc.protocol.data.game.Chunk;
import org.spacehq.mc.protocol.util.NetUtil;
import org.spacehq.mc.protocol.util.NetworkChunkData;
import org.spacehq.mc.protocol.util.ParsedChunkData;
import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.io.NetOutput;
import org.spacehq.packetlib.packet.Packet;

import java.io.IOException;

public class ServerMultiChunkDataPacket implements Packet {

    private int x[];
    private int z[];
    private Chunk chunks[][];
    private byte biomeData[][];

    @SuppressWarnings("unused")
    private ServerMultiChunkDataPacket() {
    }

    public ServerMultiChunkDataPacket(int x[], int z[], Chunk chunks[][], byte biomeData[][]) {
        if(biomeData == null) {
            throw new IllegalArgumentException("BiomeData cannot be null.");
        }

        if(x.length != chunks.length || z.length != chunks.length) {
            throw new IllegalArgumentException("X, Z, and Chunk arrays must be equal in length.");
        }

        boolean noSkylight = false;
        boolean skylight = false;
        for(int index = 0; index < chunks.length; index++) {
            Chunk column[] = chunks[index];
            if(column.length != 16) {
                throw new IllegalArgumentException("Chunk columns must contain 16 chunks each.");
            }

            for(int y = 0; y < column.length; y++) {
                if(column[y] != null) {
                    if(column[y].getSkyLight() == null) {
                        noSkylight = true;
                    } else {
                        skylight = true;
                    }
                }
            }
        }

        if(noSkylight && skylight) {
            throw new IllegalArgumentException("Either all chunks must have skylight values or none must have them.");
        }

        this.x = x;
        this.z = z;
        this.chunks = chunks;
        this.biomeData = biomeData;
    }

    public int getColumns() {
        return this.chunks.length;
    }

    public int getX(int column) {
        return this.x[column];
    }

    public int getZ(int column) {
        return this.z[column];
    }

    public Chunk[] getChunks(int column) {
        return this.chunks[column];
    }

    public byte[] getBiomeData(int column) {
        return this.biomeData[column];
    }

    @Override
    public void read(NetInput in) throws IOException {
        boolean skylight = in.readBoolean();
        int columns = in.readVarInt();
        this.x = new int[columns];
        this.z = new int[columns];
        this.chunks = new Chunk[columns][];
        this.biomeData = new byte[columns][];
        NetworkChunkData[] data = new NetworkChunkData[columns];
        for(int column = 0; column < columns; column++) {
            this.x[column] = in.readInt();
            this.z[column] = in.readInt();
            int mask = in.readUnsignedShort();
            int chunks = Integer.bitCount(mask);
            int length = (chunks * ((4096 * 2) + 2048)) + (skylight ? chunks * 2048 : 0) + 256;
            byte dat[] = new byte[length];
            data[column] = new NetworkChunkData(mask, true, skylight, dat);
        }

        for(int column = 0; column < columns; column++) {
            in.readBytes(data[column].getData());
            ParsedChunkData chunkData = NetUtil.dataToChunks(data[column], false);
            this.chunks[column] = chunkData.getChunks();
            this.biomeData[column] = chunkData.getBiomes();
        }
    }

    @Override
    public void write(NetOutput out) throws IOException {
        boolean skylight = false;
        NetworkChunkData data[] = new NetworkChunkData[this.chunks.length];
        for(int column = 0; column < this.chunks.length; column++) {
            data[column] = NetUtil.chunksToData(new ParsedChunkData(this.chunks[column], this.biomeData[column]));
            if(data[column].hasSkyLight()) {
                skylight = true;
            }
        }

        out.writeBoolean(skylight);
        out.writeVarInt(this.chunks.length);
        for(int column = 0; column < this.x.length; column++) {
            out.writeInt(this.x[column]);
            out.writeInt(this.z[column]);
            out.writeShort(data[column].getMask());
        }

        for(int column = 0; column < this.x.length; column++) {
            out.writeBytes(data[column].getData());
        }
    }

    @Override
    public boolean isPriority() {
        return false;
    }

}
