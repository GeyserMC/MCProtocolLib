package ch.spacebase.mc.protocol.packet.ingame.server.entity;

import java.io.IOException;

import ch.spacebase.packetlib.io.NetInput;
import ch.spacebase.packetlib.io.NetOutput;
import ch.spacebase.packetlib.packet.Packet;

public class ServerEntityRemoveEffectPacket implements Packet {
	
	private int entityId;
	private Effect effect;
	
	@SuppressWarnings("unused")
	private ServerEntityRemoveEffectPacket() {
	}
	
	public ServerEntityRemoveEffectPacket(int entityId, Effect effect) {
		this.entityId = entityId;
		this.effect = effect;
	}
	
	public int getEntityId() {
		return this.entityId;
	}
	
	public Effect getEffect() {
		return this.effect;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.entityId = in.readInt();
		this.effect = Effect.values()[in.readByte() - 1];
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeInt(this.entityId);
		out.writeByte(this.effect.ordinal() + 1);
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
