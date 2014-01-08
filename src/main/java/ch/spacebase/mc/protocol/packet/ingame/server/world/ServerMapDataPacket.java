package ch.spacebase.mc.protocol.packet.ingame.server.world;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ch.spacebase.packetlib.io.NetInput;
import ch.spacebase.packetlib.io.NetOutput;
import ch.spacebase.packetlib.packet.Packet;

public class ServerMapDataPacket implements Packet {
	
	private int mapId;
	private Type type;
	private MapData data; 
	
	@SuppressWarnings("unused")
	private ServerMapDataPacket() {
	}
	
	public ServerMapDataPacket(int mapId, Type type, MapData data) {
		this.mapId = mapId;
		this.type = type;
		this.data = data;
	}
	
	public int getMapId() {
		return this.mapId;
	}
	
	public Type getType() {
		return this.type;
	}
	
	public MapData getData() {
		return this.data;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.mapId = in.readVarInt();
		byte data[] = in.readBytes(in.readShort());
		this.type = Type.values()[data[0]];
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
				data[0] = 0;
				data[1] = column.getX();
				data[2] = column.getY();

				for(int index = 0; index < data.length - 3; index++) {
					data[index + 3] = column.getColors()[index];
				}

				break;
			case PLAYERS:
				MapPlayers players = (MapPlayers) this.data;
				data = new byte[players.getPlayers().size() * 3 + 1];
				data[0] = 1;
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
				data[0] = 2;
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
	
	public static enum Type {
		IMAGE,
		PLAYERS,
		SCALE;
	}
	
	public static interface MapData {
	}
	
	public static class MapColumnUpdate implements MapData {
		private byte x;
		private byte y;
		private byte height;
		private byte colors[];
		
		/**
		 * Creates a new map column update instance.
		 * @param x X of the map column.
		 * @param y Y of the data's update range.
		 * @param height Height of the data's update range.
		 * @param fullMapColors The full array of map color data, arranged in order of ascending Y value relative to the given Y value.
		 */
		public MapColumnUpdate(int x, int y, int height, byte colors[]) {
			this.x = (byte) x;
			this.y = (byte) y;
			this.height = (byte) height;
			this.colors = colors;
		}
		
		public byte getX() {
			return this.x;
		}
		
		public byte getY() {
			return this.y;
		}
		
		public byte getHeight() {
			return this.height;
		}
		
		public byte[] getColors() {
			return this.colors;
		}
	}
	
	public static class MapPlayers implements MapData {
		private List<MapPlayer> players = new ArrayList<MapPlayer>();
		
		public MapPlayers(List<MapPlayer> players) {
			this.players = players;
		}
		
		public List<MapPlayer> getPlayers() {
			return new ArrayList<MapPlayer>(this.players);
		}
	}
	
	public static class MapPlayer {
		private byte iconSize;
		private byte iconRotation;
		private byte centerX;
		private byte centerZ;
		
		public MapPlayer(int iconSize, int iconRotation, int centerX, int centerZ) {
			this.iconSize = (byte) iconSize;
			this.iconRotation = (byte) iconRotation;
			this.centerX = (byte) centerX;
			this.centerZ = (byte) centerZ;
		}
		
		public byte getIconSize() {
			return this.iconSize;
		}
		
		public byte getIconRotation() {
			return this.iconRotation;
		}
		
		public byte getCenterX() {
			return this.centerX;
		}
		
		public byte getCenterZ() {
			return this.centerZ;
		}
	}
	
	public static class MapScale implements MapData {
		private byte scale;
		
		public MapScale(int scale) {
			this.scale = (byte) scale;
		}
		
		public byte getScale() {
			return this.scale;
		}
	}

}
