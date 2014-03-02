package org.spacehq.mc.protocol.packet.ingame.server.world;

import org.spacehq.mc.protocol.data.game.values.MagicValues;
import org.spacehq.mc.protocol.data.game.values.world.map.*;
import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.io.NetOutput;
import org.spacehq.packetlib.packet.Packet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ServerMapDataPacket implements Packet {

	private int mapId;
	private MapDataType type;
	private MapData data;

	@SuppressWarnings("unused")
	private ServerMapDataPacket() {
	}

	public ServerMapDataPacket(int mapId, MapDataType type, MapData data) {
		this.mapId = mapId;
		this.type = type;
		this.data = data;
	}

	public int getMapId() {
		return this.mapId;
	}

	public MapDataType getType() {
		return this.type;
	}

	public MapData getData() {
		return this.data;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.mapId = in.readVarInt();
		byte data[] = in.readBytes(in.readShort());
		this.type = MagicValues.key(MapDataType.class, data[0]);
		switch(this.type) {
			case IMAGE:
				byte x = data[1];
				byte y = data[2];
				byte height = (byte) (data.length - 3);
				byte colors[] = new byte[height];
				for(int index = 0; index < height; index++) {
					colors[index] = data[index + 3];
				}

				this.data = new MapColumnUpdate(x, y, height, colors);
				break;
			case PLAYERS:
				List<MapPlayer> players = new ArrayList<MapPlayer>();
				for(int index = 0; index < (data.length - 1) / 3; index++) {
					byte iconSize = (byte) (data[index * 3 + 1] >> 4);
					byte iconRotation = (byte) (data[index * 3 + 1] & 15);
					byte centerX = data[index * 3 + 2];
					byte centerY = data[index * 3 + 3];
					players.add(new MapPlayer(iconSize, iconRotation, centerX, centerY));
				}

				this.data = new MapPlayers(players);
				break;
			case SCALE:
				this.data = new MapScale(data[1]);
				break;
		}
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeVarInt(this.mapId);
		byte data[] = null;
		switch(this.type) {
			case IMAGE:
				MapColumnUpdate column = (MapColumnUpdate) this.data;
				data = new byte[column.getHeight() + 4];
				data[0] = (byte) MagicValues.value(Integer.class, this.type).intValue();
				data[1] = column.getX();
				data[2] = column.getY();

				for(int index = 0; index < data.length - 3; index++) {
					data[index + 3] = column.getColors()[index];
				}

				break;
			case PLAYERS:
				MapPlayers players = (MapPlayers) this.data;
				data = new byte[players.getPlayers().size() * 3 + 1];
				data[0] = (byte) MagicValues.value(Integer.class, this.type).intValue();
				for(int index = 0; index < players.getPlayers().size(); index++) {
					MapPlayer player = players.getPlayers().get(index);
					data[index * 3 + 1] = (byte) (player.getIconSize() << 4 | player.getIconRotation() & 15);
					data[index * 3 + 2] = player.getCenterX();
					data[index * 3 + 3] = player.getCenterZ();
				}

				break;
			case SCALE:
				MapScale scale = (MapScale) this.data;
				data = new byte[2];
				data[0] = (byte) MagicValues.value(Integer.class, this.type).intValue();
				data[1] = scale.getScale();
				break;
		}

		out.writeShort(data.length);
		out.writeBytes(data);
	}

	@Override
	public boolean isPriority() {
		return false;
	}

}
