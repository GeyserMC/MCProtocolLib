package ch.spacebase.mcprotocol.standard.packet;

import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.net.ServerConnection;
import ch.spacebase.mcprotocol.net.io.NetInput;
import ch.spacebase.mcprotocol.net.io.NetOutput;
import ch.spacebase.mcprotocol.packet.Packet;
import java.io.IOException;
import java.util.concurrent.Semaphore;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class PacketMapChunk extends Packet
{
    /** The x-position of the transmitted chunk, in chunk coordinates. */
    public int xCh;

    /** The z-position of the transmitted chunk, in chunk coordinates. */
    public int zCh;

    /**
     * The y-position of the lowest chunk Section in the transmitted chunk, in chunk coordinates.
     */
    public int yChMin;

    /**
     * The y-position of the highest chunk Section in the transmitted chunk, in chunk coordinates.
     */
    public int yChMax;

    /** The transmitted chunk data, decompressed. */
    private byte[] chunkData;

    /** The compressed chunk data */
    private byte[] compressedChunkData;

    /**
     * Whether to initialize the Chunk before applying the effect of the Packet51MapChunk.
     */
    public boolean includeInitialize;

    /** The length of the compressed chunk data byte array. */
    private int tempLength;

    /** A temporary storage for the compressed chunk data byte array. */
    private static byte[] temp = new byte[196864];

    private Semaphore deflateGate;
    private final boolean isChunkDataPacket;

    public PacketMapChunk()
    {
        this.isChunkDataPacket = true;
        this.deflateGate = new Semaphore(1);
    }

    private void deflate()
    {
        Deflater deflater = new Deflater(-1);
        try
        {
            deflater.setInput(compressedChunkData, 0, compressedChunkData.length);
            deflater.finish();
            byte[] deflated = new byte[compressedChunkData.length];
            this.tempLength = deflater.deflate(deflated);
            this.chunkData = deflated;
        }
        finally
        {
            deflater.end();
        }
    }

    /**
     * Abstract. Reads the raw packet data from the data stream.
     */
    @Override
    public void read(NetInput par1DataInput) throws IOException
    {
        this.xCh = par1DataInput.readInt();
        this.zCh = par1DataInput.readInt();
        this.includeInitialize = par1DataInput.readBoolean();
        this.yChMin = par1DataInput.readShort();
        this.yChMax = par1DataInput.readShort();
        this.tempLength = par1DataInput.readInt();

        if (temp.length < this.tempLength)
        {
            temp = new byte[this.tempLength];
        }

        temp = par1DataInput.readBytes(this.tempLength);
        int i = 0;
        int j;
        int msb = 0;

        for (j = 0; j < 16; ++j)
        {
            i += this.yChMin >> j & 1;
            msb  += this.yChMax >> j & 1;
        }

        j = 12288 * i;
        j += 2048 * msb;

        if (this.includeInitialize)
        {
            j += 256;
        }

        this.compressedChunkData = new byte[j];
        Inflater inflater = new Inflater();
        inflater.setInput(temp, 0, this.tempLength);

        try
        {
            inflater.inflate(this.compressedChunkData);
        }
        catch (DataFormatException dataformatexception)
        {
            throw new IOException("Bad compressed data format");
        }
        finally
        {
            inflater.end();
        }
    }

    /**
     * Abstract. Writes the raw packet data to the data stream.
     */
    @Override
    public void write(NetOutput par1DataOutput) throws IOException
    {
        if (chunkData == null)
        {
            deflateGate.acquireUninterruptibly();
            if (chunkData == null)
            {
                deflate();
            }
            deflateGate.release();
        }

        par1DataOutput.writeInt(this.xCh);
        par1DataOutput.writeInt(this.zCh);
        par1DataOutput.writeBoolean(this.includeInitialize);
        par1DataOutput.writeShort((short)(this.yChMin & 65535));
        par1DataOutput.writeShort((short)(this.yChMax & 65535));
        par1DataOutput.writeInt(this.tempLength);
        par1DataOutput.writeBytes(this.chunkData, this.tempLength);
    }

    @Override
    public void handleClient(Client conn) {
        
    }

    @Override
    public void handleServer(ServerConnection conn) {
        
    }

    @Override
    public int getId() {
        return 51;
    }
}
