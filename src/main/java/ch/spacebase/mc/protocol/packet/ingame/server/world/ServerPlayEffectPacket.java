package ch.spacebase.mc.protocol.packet.ingame.server.world;

import java.io.IOException;

import ch.spacebase.packetlib.io.NetInput;
import ch.spacebase.packetlib.io.NetOutput;
import ch.spacebase.packetlib.packet.Packet;

public class ServerPlayEffectPacket implements Packet {

	private Effect effect;
	private int x;
	private int y;
	private int z;
	private EffectData data;
	private boolean broadcast;
	
	@SuppressWarnings("unused")
	private ServerPlayEffectPacket() {
	}
	
	public ServerPlayEffectPacket(Effect effect, int x, int y, int z, EffectData data) {
		this(effect, x, y, z, data, false);
	}
	
	public ServerPlayEffectPacket(Effect effect, int x, int y, int z, EffectData data, boolean broadcast) {
		this.effect = effect;
		this.x = x;
		this.y = y;
		this.z = z;
		this.data = data;
		this.broadcast = broadcast;
	}
	
	public Effect getEffect() {
		return this.effect;
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
	
	public EffectData getData() {
		return this.data;
	}
	
	public boolean getBroadcast() {
		return this.broadcast;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.effect = this.idToEffect(in.readInt());
		this.x = in.readInt();
		this.y = in.readUnsignedByte();
		this.z = in.readInt();
		this.data = this.valueToData(in.readInt());
		this.broadcast = in.readBoolean();
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeInt(this.effectToId(this.effect));
		out.writeInt(this.x);
		out.writeByte(this.y);
		out.writeInt(this.z);
		out.writeInt(this.dataToValue(this.data));
		out.writeBoolean(this.broadcast);
	}
	
	@Override
	public boolean isPriority() {
		return false;
	}
	
	private Effect idToEffect(int id) throws IOException {
		switch(id) {
			case 1000:
				return SoundEffect.CLICK;
			case 1001:
				return SoundEffect.EMPTY_DISPENSER_CLICK;
			case 1002:
				return SoundEffect.FIRE_PROJECTILE;
			case 1003:
				return SoundEffect.DOOR;
			case 1004:
				return SoundEffect.FIZZLE;
			case 1005:
				return SoundEffect.PLAY_RECORD;
			case 1007:
				return SoundEffect.GHAST_CHARGE;
			case 1008:
				return SoundEffect.GHAST_FIRE;
			case 1009:
				return SoundEffect.BLAZE_FIRE;
			case 1010:
				return SoundEffect.POUND_WOODEN_DOOR;
			case 1011:
				return SoundEffect.POUND_METAL_DOOR;
			case 1012:
				return SoundEffect.BREAK_WOODEN_DOOR;
			case 1014:
				return SoundEffect.WITHER_SHOOT;
			case 1015:
				return SoundEffect.BAT_TAKE_OFF;
			case 1016:
				return SoundEffect.INFECT_VILLAGER;
			case 1017:
				return SoundEffect.DISINFECT_VILLAGER;
			case 1018:
				return SoundEffect.ENDER_DRAGON_DEATH;
			case 1020:
				return SoundEffect.ANVIL_BREAK;
			case 1021:
				return SoundEffect.ANVIL_USE;
			case 1022:
				return SoundEffect.ANVIL_LAND;
		}
		
		switch(id) {
			case 2000:
				return ParticleEffect.SMOKE;
			case 2001:
				return ParticleEffect.BREAK_BLOCK;
			case 2002:
				return ParticleEffect.BREAK_SPLASH_POTION;
			case 2003:
				return ParticleEffect.BREAK_EYE_OF_ENDER;
			case 2004:
				return ParticleEffect.MOB_SPAWN;
			case 2005:
				return ParticleEffect.BONEMEAL_GROW;
			case 2006:
				return ParticleEffect.HARD_LANDING_DUST;
		}
		
		throw new IOException("Unknown effect id: " + id);
	}
	
	private int effectToId(Effect effect) throws IOException {
		if(effect == SoundEffect.CLICK) {
			return 1000;
		} else if(effect == SoundEffect.EMPTY_DISPENSER_CLICK) {
			return 1001;
		} else if(effect == SoundEffect.FIRE_PROJECTILE) {
			return 1002;
		} else if(effect == SoundEffect.DOOR) {
			return 1003;
		} else if(effect == SoundEffect.FIZZLE) {
			return 1004;
		} else if(effect == SoundEffect.PLAY_RECORD) {
			return 1005;
		} else if(effect == SoundEffect.GHAST_CHARGE) {
			return 1007;
		} else if(effect == SoundEffect.GHAST_FIRE) {
			return 1008;
		} else if(effect == SoundEffect.BLAZE_FIRE) {
			return 1009;
		} else if(effect == SoundEffect.POUND_WOODEN_DOOR) {
			return 1010;
		} else if(effect == SoundEffect.POUND_METAL_DOOR) {
			return 1011;
		} else if(effect == SoundEffect.BREAK_WOODEN_DOOR) {
			return 1012;
		} else if(effect == SoundEffect.WITHER_SHOOT) {
			return 1014;
		} else if(effect == SoundEffect.BAT_TAKE_OFF) {
			return 1015;
		} else if(effect == SoundEffect.INFECT_VILLAGER) {
			return 1016;
		} else if(effect == SoundEffect.DISINFECT_VILLAGER) {
			return 1017;
		} else if(effect == SoundEffect.ENDER_DRAGON_DEATH) {
			return 1018;
		} else if(effect == SoundEffect.ANVIL_BREAK) {
			return 1020;
		} else if(effect == SoundEffect.ANVIL_USE) {
			return 1021;
		} else if(effect == SoundEffect.ANVIL_LAND) {
			return 1022;
		}
		
		if(effect == ParticleEffect.SMOKE) {
			return 2000;
		} else if(effect == ParticleEffect.BREAK_BLOCK) {
			return 2001;
		} else if(effect == ParticleEffect.BREAK_SPLASH_POTION) {
			return 2002;
		} else if(effect == ParticleEffect.BREAK_EYE_OF_ENDER) {
			return 2003;
		} else if(effect == ParticleEffect.MOB_SPAWN) {
			return 2004;
		} else if(effect == ParticleEffect.BONEMEAL_GROW) {
			return 2005;
		} else if(effect == ParticleEffect.HARD_LANDING_DUST) {
			return 2006;
		}
		
		throw new IOException("Unmapped effect: " + effect);
	}
	
	private EffectData valueToData(int value) {
		if(this.effect == SoundEffect.PLAY_RECORD) {
			return new RecordData(value);
		} else if(this.effect == ParticleEffect.SMOKE) {
			if(value == 0) {
				return SmokeData.SOUTH_EAST;
			} else if(value == 1) {
				return SmokeData.SOUTH;
			} else if(value == 2) {
				return SmokeData.SOUTH_WEST;
			} else if(value == 3) {
				return SmokeData.EAST;
			} else if(value == 4) {
				return SmokeData.UP;
			} else if(value == 5) {
				return SmokeData.WEST;
			} else if(value == 6) {
				return SmokeData.NORTH_EAST;
			} else if(value == 7) {
				return SmokeData.NORTH;
			} else if(value == 8) {
				return SmokeData.NORTH_WEST;
			}
		} else if(this.effect == ParticleEffect.BREAK_BLOCK) {
			return new BreakBlockData(value);
		} else if(this.effect == ParticleEffect.BREAK_SPLASH_POTION) {
			return new BreakPotionData(value);
		} else if(this.effect == ParticleEffect.HARD_LANDING_DUST) {
			return new HardLandingData(value);
		}
		
		return null;
	}
	
	private int dataToValue(EffectData data) {
		if(data instanceof RecordData) {
			return ((RecordData) data).getRecordId();
		}
		
		if(data instanceof SmokeData) {
			if(data == SmokeData.SOUTH_EAST) {
				return 0;
			} else if(data == SmokeData.SOUTH) {
				return 1;
			} else if(data == SmokeData.SOUTH_WEST) {
				return 2;
			} else if(data == SmokeData.EAST) {
				return 3;
			} else if(data == SmokeData.UP) {
				return 4;
			} else if(data == SmokeData.WEST) {
				return 5;
			} else if(data == SmokeData.NORTH_EAST) {
				return 6;
			} else if(data == SmokeData.NORTH) {
				return 7;
			} else if(data == SmokeData.NORTH_WEST) {
				return 8;
			}
		}
		
		if(data instanceof BreakBlockData) {
			return ((BreakBlockData) data).getBlockId();
		}
		
		if(data instanceof BreakPotionData) {
			return ((BreakPotionData) data).getPotionId();
		}
		
		if(data instanceof HardLandingData) {
			return ((HardLandingData) data).getDamagingDistance();
		}
		
		return 0;
	}
	
	public static interface Effect {
	}

	public static enum SoundEffect implements Effect {
		CLICK,
		EMPTY_DISPENSER_CLICK,
		FIRE_PROJECTILE,
		DOOR,
		FIZZLE,
		PLAY_RECORD,
		GHAST_CHARGE,
		GHAST_FIRE,
		BLAZE_FIRE,
		POUND_WOODEN_DOOR,
		POUND_METAL_DOOR,
		BREAK_WOODEN_DOOR,
		WITHER_SHOOT,
		BAT_TAKE_OFF,
		INFECT_VILLAGER,
		DISINFECT_VILLAGER,
		ENDER_DRAGON_DEATH,
		ANVIL_BREAK,
		ANVIL_USE,
		ANVIL_LAND;
	}
	
	public static enum ParticleEffect implements Effect {
		SMOKE,
		BREAK_BLOCK,
		BREAK_SPLASH_POTION,
		BREAK_EYE_OF_ENDER,
		MOB_SPAWN,
		BONEMEAL_GROW,
		HARD_LANDING_DUST;
	}
	
	public static interface EffectData {
	}
	
	public static class RecordData implements EffectData {
		private int recordId;
		
		public RecordData(int recordId) {
			this.recordId = recordId;
		}
		
		public int getRecordId() {
			return this.recordId;
		}
	}
	
	public static enum SmokeData implements EffectData {
		SOUTH_EAST,
		SOUTH,
		SOUTH_WEST,
		EAST,
		UP,
		WEST,
		NORTH_EAST,
		NORTH,
		NORTH_WEST;
	}
	
	public static class BreakBlockData implements EffectData {
		private int blockId;
		
		public BreakBlockData(int blockId) {
			this.blockId = blockId;
		}
		
		public int getBlockId() {
			return this.blockId;
		}
	}
	
	public static class BreakPotionData implements EffectData {
		private int potionId;
		
		public BreakPotionData(int potionId) {
			this.potionId = potionId;
		}
		
		public int getPotionId() {
			return this.potionId;
		}
	}
	
	public static class HardLandingData implements EffectData {
		private int damagingDistance;
		
		public HardLandingData(int damagingDistance) {
			this.damagingDistance = damagingDistance;
		}
		
		public int getDamagingDistance() {
			return this.damagingDistance;
		}
	}
	
}
