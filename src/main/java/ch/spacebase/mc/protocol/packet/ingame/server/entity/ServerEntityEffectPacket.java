package ch.spacebase.mc.protocol.packet.ingame.server.entity;

import java.io.IOException;

import ch.spacebase.packetlib.io.NetInput;
import ch.spacebase.packetlib.io.NetOutput;
import ch.spacebase.packetlib.packet.Packet;

public class ServerEntityEffectPacket implements Packet {
	
	private int entityId;
	private Effect effect;
	private int amplifier;
	private int duration;
	
	@SuppressWarnings("unused")
	private ServerEntityEffectPacket() {
	}
	
	public ServerEntityEffectPacket(int entityId, Effect effect, int amplifier, int duration) {
		this.entityId = entityId;
		this.effect = effect;
		this.amplifier = amplifier;
		this.duration = duration;
	}
	
	public int getEntityId() {
		return this.entityId;
	}
	
	public Effect getEffect() {
		return this.effect;
	}
	
	public int getAmplifier() {
		return this.amplifier;
	}
	
	public int getDuration() {
		return this.duration;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.entityId = in.readInt();
		this.effect = Effect.values()[in.readByte() - 1];
		this.amplifier = in.readByte();
		this.duration = in.readShort();
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeInt(this.entityId);
		out.writeByte(this.effect.ordinal() + 1);
		out.writeByte(this.amplifier);
		out.writeShort(this.duration);
	}
	
	@Override
	public boolean isPriority() {
		return false;
	}
	
	public static enum Effect {
		SPEED,
		SLOWNESS,
		DIG_SPEED,
		DIG_SLOWNESS,
		DAMAGE_BOOST,
		HEAL,
		DAMAGE,
		ENHANCED_JUMP,
		CONFUSION,
		REGENERATION,
		RESISTANCE,
		FIRE_RESISTANCE,
		WATER_BREATHING,
		INVISIBILITY,
		BLINDNESS,
		NIGHT_VISION,
		HUNGER,
		WEAKNESS,
		POISON,
		WITHER_EFFECT,
		HEALTH_BOOST,
		ABSORPTION,
		SATURATION;
	}

}
