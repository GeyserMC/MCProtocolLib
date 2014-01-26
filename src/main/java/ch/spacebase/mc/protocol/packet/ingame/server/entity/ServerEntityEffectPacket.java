package ch.spacebase.mc.protocol.packet.ingame.server.entity;

import java.io.IOException;

import ch.spacebase.mc.protocol.data.game.values.Effect;
import ch.spacebase.mc.protocol.data.game.values.MagicValues;
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
		this.entityId = in.readVarInt();
		this.effect = MagicValues.key(Effect.class, in.readByte());
		this.amplifier = in.readByte();
		this.duration = in.readVarInt();
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeVarInt(this.entityId);
		out.writeByte(MagicValues.value(Integer.class, this.effect));
		out.writeByte(this.amplifier);
		out.writeVarInt(this.duration);
	}
	
	@Override
	public boolean isPriority() {
		return false;
	}

}
