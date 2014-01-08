package ch.spacebase.mc.protocol.packet.ingame.server.world;

import java.io.IOException;

import ch.spacebase.packetlib.io.NetInput;
import ch.spacebase.packetlib.io.NetOutput;
import ch.spacebase.packetlib.packet.Packet;

public class ServerBlockValuePacket implements Packet {
	
	private static final int NOTE_BLOCK = 25;
	private static final int STICKY_PISTON = 29;
	private static final int PISTON = 33;
	private static final int MOB_SPAWNER = 52;
	private static final int CHEST = 54;
	private static final int ENDER_CHEST = 130;
	private static final int TRAPPED_CHEST = 146;
	
	private int x;
	private int y;
	private int z;
	private ValueType type;
	private Value value;
	private int blockId;
	
	@SuppressWarnings("unused")
	private ServerBlockValuePacket() {
	}
	
	public ServerBlockValuePacket(int x, int y, int z, ValueType type, Value value, int blockId) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.type = type;
		this.value = value;
		this.blockId = blockId;
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
	public int getZ() {
		return this.z;
	}
	
	public ValueType getType() {
		return this.type;
	}
	
	public Value getValue() {
		return this.value;
	}
	
	public int getBlockId() {
		return this.blockId;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.x = in.readInt();
		this.y = in.readShort();
		this.z = in.readInt();
		this.type = intToType(in.readUnsignedByte());
		this.value = intToValue(in.readUnsignedByte());
		this.blockId = in.readVarInt() & 4095;
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeInt(this.x);
		out.writeShort(this.y);
		out.writeInt(this.z);
		out.writeByte(typeToInt(this.type));
		out.writeByte(valueToInt(this.value));
		out.writeVarInt(this.blockId & 4095);
	}
	
	@Override
	public boolean isPriority() {
		return false;
	}
	
	private ValueType intToType(int value) throws IOException {
		if(this.blockId == NOTE_BLOCK) {
			if(value == 0) {
				return NoteBlockValueType.HARP;
			} else if(value == 1) {
				return NoteBlockValueType.DOUBLE_BASS;
			} else if(value == 2) {
				return NoteBlockValueType.SNARE_DRUM;
			} else if(value == 3) {
				return NoteBlockValueType.HI_HAT;
			} else if(value == 4) {
				return NoteBlockValueType.BASS_DRUM;
			}
		} else if(this.blockId == STICKY_PISTON || this.blockId == PISTON) {
			if(value == 0) {
				return PistonValueType.PUSHING;
			} else if(value == 1) {
				return PistonValueType.PULLING;
			}
		} else if(this.blockId == MOB_SPAWNER) {
			if(value == 1) {
				return MobSpawnerValueType.RESET_DELAY;
			}
		} else if(this.blockId == CHEST || this.blockId == ENDER_CHEST || this.blockId == TRAPPED_CHEST) {
			if(value == 1) {
				return ChestValueType.VIEWING_PLAYER_COUNT;
			}
		} else {
			return GenericValueType.GENERIC;
		}
		
		throw new IOException("Unknown value type id: " + value + ", " + this.blockId);
	}
	
	private int typeToInt(ValueType type) throws IOException {
		if(type == NoteBlockValueType.HARP) {
			return 0;
		} else if(type == NoteBlockValueType.DOUBLE_BASS) {
			return 1;
		} else if(type == NoteBlockValueType.SNARE_DRUM) {
			return 2;
		} else if(type == NoteBlockValueType.HI_HAT) {
			return 3;
		} else if(type == NoteBlockValueType.BASS_DRUM) {
			return 4;
		}
		
		if(type == PistonValueType.PUSHING) {
			return 0;
		} else if(type == PistonValueType.PULLING) {
			return 1;
		}
		
		if(type == MobSpawnerValueType.RESET_DELAY) {
			return 1;
		}
		
		if(type == ChestValueType.VIEWING_PLAYER_COUNT) {
			return 1;
		}
		
		if(type == GenericValueType.GENERIC) {
			return 0;
		}
		
		throw new IOException("Unmapped value type: " + type);
	}
	
	private Value intToValue(int value) throws IOException {
		if(this.blockId == NOTE_BLOCK) {
			return new NoteBlockValue(value);
		} else if(this.blockId == STICKY_PISTON || this.blockId == PISTON) {
			if(value == 0) {
				return PistonValue.DOWN;
			} else if(value == 1) {
				return PistonValue.UP;
			} else if(value == 2) {
				return PistonValue.SOUTH;
			} else if(value == 3) {
				return PistonValue.WEST;
			} else if(value == 4) {
				return PistonValue.NORTH;
			} else if(value == 5) {
				return PistonValue.EAST;
			}
		} else if(this.blockId == MOB_SPAWNER) {
			if(value == 0) {
				return MobSpawnerValue.VALUE;
			}
		} else if(this.blockId == CHEST || this.blockId == ENDER_CHEST || this.blockId == TRAPPED_CHEST) {
			return new ChestValue(value);
		} else {
			return new GenericValue(value);
		}
		
		throw new IOException("Unknown value id: " + value + ", " + this.blockId);
	}
	
	private int valueToInt(Value value) throws IOException {
		if(value instanceof NoteBlockValue) {
			return ((NoteBlockValue) value).getPitch();
		}
		
		if(value == PistonValue.DOWN) {
			return 0;
		} else if(value == PistonValue.UP) {
			return 1;
		} else if(value == PistonValue.SOUTH) {
			return 2;
		} else if(value == PistonValue.WEST) {
			return 3;
		} else if(value == PistonValue.NORTH) {
			return 4;
		} else if(value == PistonValue.EAST) {
			return 5;
		}
		
		if(value == MobSpawnerValue.VALUE) {
			return 0;
		}
		
		if(value instanceof ChestValue) {
			return ((ChestValue) value).getViewers();
		}
		
		if(value instanceof GenericValue) {
			return ((GenericValue) value).getValue();
		}
		
		throw new IOException("Unmapped value: " + value);
	}
	
	public static interface ValueType {
	}
	
	public static enum GenericValueType implements ValueType {
		GENERIC;
	}
	
	public static enum NoteBlockValueType implements ValueType {
		HARP,
		DOUBLE_BASS,
		SNARE_DRUM,
		HI_HAT,
		BASS_DRUM;
	}
	
	public static enum PistonValueType implements ValueType {
		PUSHING,
		PULLING;
	}
	
	public static enum ChestValueType implements ValueType {
		VIEWING_PLAYER_COUNT;
	}
	
	public static enum MobSpawnerValueType implements ValueType {
		RESET_DELAY;
	}
	
	public static interface Value {
	}
	
	public static class GenericValue implements Value {
		private int value;
		
		public GenericValue(int value) {
			this.value = value;
		}
		
		public int getValue() {
			return this.value;
		}
	}
	
	public static class NoteBlockValue implements Value {
		private int pitch;
		
		public NoteBlockValue(int pitch) {
			if(pitch < 0 || pitch > 24) {
				throw new IllegalArgumentException("Pitch must be between 0 and 24.");
			}
			
			this.pitch = pitch;
		}
		
		public int getPitch() {
			return this.pitch;
		}
	}
	
	public static enum PistonValue implements Value {
		DOWN,
		UP,
		SOUTH,
		WEST,
		NORTH,
		EAST;
	}
	
	public static class ChestValue implements Value {
		private int viewers;
		
		public ChestValue(int viewers) {
			this.viewers = viewers;
		}
		
		public int getViewers() {
			return this.viewers;
		}
	}
	
	public static enum MobSpawnerValue implements Value {
		VALUE;
	}

}
