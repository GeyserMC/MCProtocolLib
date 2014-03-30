package org.spacehq.mc.protocol.packet.ingame.server.world;

import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.io.NetOutput;
import org.spacehq.packetlib.packet.Packet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
				int x = data[1] & 0xFF;
				int y = data[2] & 0xFF;
				int height = data.length - 3;
				byte colors[] = new byte[height];
				for(int index = 0; index < height; index++) {
					colors[index] = data[index + 3];
				}

				this.data = new MapColumnUpdate(x, y, height, colors);
				break;
			case PLAYERS:
				List<MapPlayer> players = new ArrayList<MapPlayer>();
				for(int index = 0; index < (data.length - 1) / 3; index++) {
					int sizeRot = data[index * 3 + 1] & 0xFF;
					int iconSize = (sizeRot >> 4) & 0xFF;
					int iconRotation = (sizeRot & 15) & 0xFF;
					int centerX = data[index * 3 + 2] & 0xFF;
					int centerY = data[index * 3 + 3] & 0xFF;
					players.add(new MapPlayer(iconSize, iconRotation, centerX, centerY));
				}
				
				this.data = new MapPlayers(players);
				break;
			case SCALE:
				this.data = new MapScale(data[1] & 0xFF);
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
				data = new byte[column.getHeight() + 3];
				data[0] = 0;
				data[1] = (byte) column.getX();
				data[2] = (byte) column.getY();
				for(int index = 3; index < data.length; index++) {
					data[index] = column.getColors()[index - 3];
				}

				break;
			case PLAYERS:
				MapPlayers players = (MapPlayers) this.data;
				data = new byte[players.getPlayers().size() * 3 + 1];
				data[0] = 1;
				for(int index = 0; index < players.getPlayers().size(); index++) {
					MapPlayer player = players.getPlayers().get(index);
					data[index * 3 + 1] = (byte) (((byte) player.getIconSize()) << 4 | ((byte) player.getIconRotation()) & 15);
					data[index * 3 + 2] = (byte) player.getCenterX();
					data[index * 3 + 3] = (byte) player.getCenterZ();
				}
				
				break;
			case SCALE:
				MapScale scale = (MapScale) this.data;
				data = new byte[2];
				data[0] = 2;
				data[1] = (byte) scale.getScale();
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
		private int x;
		private int y;
		private int height;
		private byte colors[];
		
		/**
		 * Creates a new map column update instance.
		 * @param x X of the map column.
		 * @param y Y of the data's update range.
		 * @param height Height of the data's update range.
		 * @param colors The array of map color data, arranged in order of ascending Y value relative to the given Y value.
		 */
		public MapColumnUpdate(int x, int y, int height, byte colors[]) {
			this.x = x;
			this.y = y;
			this.height = height;
			this.colors = colors;
		}
		
		public int getX() {
			return this.x;
		}
		
		public int getY() {
			return this.y;
		}
		
		public int getHeight() {
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
		private int iconSize;
		private int iconRotation;
		private int centerX;
		private int centerZ;
		
		public MapPlayer(int iconSize, int iconRotation, int centerX, int centerZ) {
			this.iconSize = iconSize;
			this.iconRotation = iconRotation;
			this.centerX = centerX;
			this.centerZ = centerZ;
		}
		
		public int getIconSize() {
			return this.iconSize;
		}
		
		public int getIconRotation() {
			return this.iconRotation;
		}
		
		public int getCenterX() {
			return this.centerX;
		}
		
		public int getCenterZ() {
			return this.centerZ;
		}
	}
	
	public static class MapScale implements MapData {
		private int scale;
		
		public MapScale(int scale) {
			this.scale = scale;
		}
		
		public int getScale() {
			return this.scale;
		}
	}

}
