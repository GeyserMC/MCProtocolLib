package ch.spacebase.mc.protocol.packet.ingame.server.entity;

import java.io.IOException;

import ch.spacebase.mc.protocol.data.game.values.MagicValues;
import ch.spacebase.mc.protocol.data.game.values.entity.Effect;
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
		this.effect = MagicValues.key(Effect.class, in.readByte());
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeInt(this.entityId);
		out.writeByte(MagicValues.value(Integer.class, this.effect));
	}
	
	@Override
	public boolean isPriority() {
		return false;
	}

}
