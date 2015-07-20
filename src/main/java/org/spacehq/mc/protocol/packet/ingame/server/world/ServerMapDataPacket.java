package org.spacehq.mc.protocol.packet.ingame.server.world;

import org.spacehq.mc.protocol.data.game.values.world.map.MapData;
import org.spacehq.mc.protocol.data.game.values.world.map.MapPlayer;
import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.io.NetOutput;
import org.spacehq.packetlib.packet.Packet;

import java.io.IOException;

public class ServerMapDataPacket implements Packet {

    private int mapId;
    private byte scale;
    private MapPlayer players[];

    private MapData data;

    @SuppressWarnings("unused")
    private ServerMapDataPacket() {
    }

    public ServerMapDataPacket(int mapId, byte scale, MapPlayer players[]) {
        this(mapId, scale, players, null);
    }

    public ServerMapDataPacket(int mapId, byte scale, MapPlayer players[], MapData data) {
        this.mapId = mapId;
        this.scale = scale;
        this.players = players;
        this.data = data;
    }

    public int getMapId() {
        return this.mapId;
    }

    public byte getScale() {
        return this.scale;
    }

    public MapPlayer[] getPlayers() {
        return this.players;
    }

    public MapData getData() {
        return this.data;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.mapId = in.readVarInt();
        this.scale = in.readByte();
        this.players = new MapPlayer[in.readVarInt()];
        for(int index = 0; index < this.players.length; index++) {
            int data = in.readUnsignedByte();
            int size = (data >> 4) & 15;
            int rotation = data & 15;
            int x = in.readUnsignedByte();
            int z = in.readUnsignedByte();
            this.players[index] = new MapPlayer(x, z, size, rotation);
        }

        int columns = in.readUnsignedByte();
        if(columns > 0) {
            int rows = in.readUnsignedByte();
            int x = in.readUnsignedByte();
            int y = in.readUnsignedByte();
            byte data[] = in.readBytes(in.readVarInt());
            this.data = new MapData(columns, rows, x, y, data);
        }
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(this.mapId);
        out.writeByte(this.scale);
        out.writeVarInt(this.players.length);
        for(int index = 0; index < this.players.length; index++) {
            MapPlayer player = this.players[index];
            out.writeByte((player.getIconSize() & 15) << 4 | player.getIconRotation() & 15);
            out.writeByte(player.getCenterX());
            out.writeByte(player.getCenterZ());
        }

        if(this.data != null && this.data.getColumns() != 0) {
            out.writeByte(this.data.getColumns());
            out.writeByte(this.data.getRows());
            out.writeByte(this.data.getX());
            out.writeByte(this.data.getY());
            out.writeVarInt(this.data.getData().length);
            out.writeBytes(this.data.getData());
        } else {
            out.writeByte(0);
        }
    }

    @Override
    public boolean isPriority() {
        return false;
    }

}
