package org.spacehq.mc.protocol.packet.ingame.server.world;

import org.spacehq.mc.protocol.data.game.chunk.Column;
import org.spacehq.mc.protocol.util.NetUtil;
import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.io.NetOutput;
import org.spacehq.packetlib.io.buffer.ByteBufferNetOutput;
import org.spacehq.packetlib.packet.Packet;

import java.io.IOException;
import java.nio.ByteBuffer;

public class ServerMultiChunkDataPacket implements Packet {

    private Column columns[];

    @SuppressWarnings("unused")
    private ServerMultiChunkDataPacket() {
    }

    public ServerMultiChunkDataPacket(Column columns[]) {
        boolean noSkylight = false;
        boolean skylight = false;
        for(Column column : columns) {
            if(!column.hasSkylight()) {
                noSkylight = true;
            } else {
                skylight = true;
            }
        }

        if(noSkylight && skylight) {
            throw new IllegalArgumentException("Either all columns must have skylight values or none must have them.");
        }

        this.columns = columns;
    }

    public Column[] getColumns() {
        return this.columns;
    }

    @Override
    public void read(NetInput in) throws IOException {
        boolean skylight = in.readBoolean();
        int x[] = in.readInts(in.readVarInt());
        int z[] = in.readInts(in.readVarInt());
        int masks[] = in.readInts(in.readVarInt());
        byte data[] = in.readBytes(in.readVarInt());

        Column columns[] = new Column[x.length];
        for(int i = 0; i < columns.length; i++) {
            columns[i] = NetUtil.readColumn(data, x[i], z[i], true, skylight, masks[i]);
        }
    }

    @Override
    public void write(NetOutput out) throws IOException {
        Column columns[] = new Column[1];

        boolean skylight = false;
        for(Column column : columns) {
            if(column.hasSkylight()) {
                skylight = true;
                break;
            }
        }

        ByteBufferNetOutput byteOut = new ByteBufferNetOutput(ByteBuffer.allocate(columns.length * 557312));

        out.writeBoolean(skylight);

        out.writeVarInt(columns.length);
        for(Column column : columns) {
            out.writeInt(column.getX());
        }

        out.writeVarInt(columns.length);
        for(Column column : columns) {
            out.writeInt(column.getZ());
        }

        out.writeVarInt(columns.length);
        for(Column column : columns) {
            out.writeInt(NetUtil.writeColumn(byteOut, column, true, skylight));
        }

        out.writeVarInt(byteOut.getByteBuffer().arrayOffset());
        out.writeBytes(byteOut.getByteBuffer().array(), byteOut.getByteBuffer().arrayOffset());
    }

    @Override
    public boolean isPriority() {
        return false;
    }

}
