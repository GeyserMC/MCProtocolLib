package org.spacehq.mc.protocol.packet.ingame.server.world;

import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.io.NetOutput;
import org.spacehq.packetlib.packet.Packet;

import java.io.IOException;

public class ServerSpawnParticlePacket implements Packet {

	private String particle;
	private float x;
	private float y;
	private float z;
	private float offsetX;
	private float offsetY;
	private float offsetZ;
	private float velocityOffset;
	private int amount;
	
	@SuppressWarnings("unused")
	private ServerSpawnParticlePacket() {
	}
	
	public ServerSpawnParticlePacket(String particle, float x, float y, float z, float offsetX, float offsetY, float offsetZ, float velocityOffset, int amount) {
		this.particle = particle;
		this.x = x;
		this.y = y;
		this.z = z;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.offsetZ = offsetZ;
		this.velocityOffset = velocityOffset;
		this.amount = amount;
	}
	
	public String getParticle() {
		return this.particle;
	}
	
	public float getX() {
		return this.x;
	}
	
	public float getY() {
		return this.y;
	}
	
	public float getZ() {
		return this.z;
	}
	
	public float getOffsetX() {
		return this.offsetX;
	}
	
	public float getOffsetY() {
		return this.offsetY;
	}
	
	public float getOffsetZ() {
		return this.offsetZ;
	}
	
	public float getVelocityOffset() {
		return this.velocityOffset;
	}
	
	public int getAmount() {
		return this.amount;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.particle = in.readString();
		this.x = in.readFloat();
		this.y = in.readFloat();
		this.z = in.readFloat();
		this.offsetX = in.readFloat();
		this.offsetY = in.readFloat();
		this.offsetZ = in.readFloat();
		this.velocityOffset = in.readFloat();
		this.amount = in.readInt();
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeString(this.particle);
		out.writeFloat(this.x);
		out.writeFloat(this.y);
		out.writeFloat(this.z);
		out.writeFloat(this.offsetX);
		out.writeFloat(this.offsetY);
		out.writeFloat(this.offsetZ);
		out.writeFloat(this.velocityOffset);
		out.writeInt(this.amount);
	}
	
	@Override
	public boolean isPriority() {
		return false;
	}
	
	public static class Particle {
		public static final String HUGE_EXPLOSION = "hugeexplosion";
		public static final String LARGE_EXPLOSION = "largeexplode";
		public static final String FIREWORKS_SPARK = "fireworksSpark";
		public static final String LIQUID_PARTICLES = "suspended";
		public static final String DEPTH_PARTICLES = "depthsuspend";
		public static final String MYCELIUM_PARTICLES = "townaura";
		public static final String CRITICAL_HIT = "crit";
		public static final String ENCHANTED_CRITICAL_HIT = "magicCrit";
		public static final String SMOKE = "smoke";
		public static final String MOB_POTION_EFFECT = "mobSpell";
		public static final String MOB_POTION_EFFECT_AMBIENT = "mobSpellAmbient";
		public static final String POTION_EFFECT = "spell";
		public static final String INSTANT_POTION_EFFECT = "instantSpell";
		public static final String WITCH_PARTICLES = "witchMagic";
		public static final String NOTE = "note";
		public static final String PORTAL = "portal";
		public static final String ENCHANTMENT_TABLE_LETTERS = "enchantmenttable";
		public static final String EXPLOSION = "explode";
		public static final String FLAME = "flame";
		public static final String LAVA_PARTICLES = "lava";
		public static final String FOOTSTEP_PARTICLES = "footstep";
		public static final String SPLASH = "splash";
		public static final String FISH_HOOK_WAKE = "wake";
		public static final String LARGE_SMOKE = "largesmoke";
		public static final String CLOUD = "cloud";
		public static final String REDSTONE_PARTICLES = "reddust";
		public static final String BREAKING_SNOWBALL = "snowballpoof";
		public static final String DRIP_WATER = "dripWater";
		public static final String DRIP_LAVA = "dripLava";
		public static final String SHOVEL_SNOW = "snowshovel";
		public static final String SLIME = "slime";
		public static final String HEART = "heart";
		public static final String ANGRY_VILLAGER = "angryVillager";
		public static final String HAPPY_VILLAGER = "happyVillager";
		
		private static final String ITEM_BREAK_PREFIX = "iconcrack_";
		private static final String BLOCK_BREAK_PREFIX = "blockcrack_";
		private static final String BLOCK_IMPACT_PREFIX = "blockdust_";
		
		public static final String ITEM_BREAK_PARTICLES(int id) {
			return ITEM_BREAK_PARTICLES(id, -1);
		}
		
		public static final String ITEM_BREAK_PARTICLES(int id, int data) {
			return ITEM_BREAK_PREFIX + id + (data != -1 ? "_" + data : "");
		}
		
		public static final String BLOCK_BREAK_PARTICLES(int id) {
			return BLOCK_BREAK_PARTICLES(id, -1);
		}
		
		public static final String BLOCK_BREAK_PARTICLES(int id, int data) {
			return BLOCK_BREAK_PREFIX + id + (data != -1 ? "_" + data : "");
		}
		
		public static final String BLOCK_IMPACT_PARTICLES(int id) {
			return BLOCK_IMPACT_PARTICLES(id, -1);
		}
		
		public static final String BLOCK_IMPACT_PARTICLES(int id, int data) {
			return BLOCK_IMPACT_PREFIX + id + (data != -1 ? "_" + data : "");
		}
	}
	
}
