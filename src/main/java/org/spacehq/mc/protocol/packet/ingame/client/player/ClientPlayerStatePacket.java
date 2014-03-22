package org.spacehq.mc.protocol.packet.ingame.client.player;

import org.spacehq.mc.protocol.data.game.values.MagicValues;
import org.spacehq.mc.protocol.data.game.values.entity.player.PlayerState;
import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.io.NetOutput;
import org.spacehq.packetlib.packet.Packet;

import java.io.IOException;

public class ClientPlayerStatePacket implements Packet {

	private int entityId;
	private PlayerState state;
	private int jumpBoost;

	@SuppressWarnings("unused")
	private ClientPlayerStatePacket() {
	}

	public ClientPlayerStatePacket(int entityId, PlayerState state) {
		this(entityId, state, 0);
	}

	public ClientPlayerStatePacket(int entityId, PlayerState state, int jumpBoost) {
		this.entityId = entityId;
		this.state = state;
		this.jumpBoost = jumpBoost;
	}

	public int getEntityId() {
		return this.entityId;
	}

	public PlayerState getState() {
		return this.state;
	}

	public int getJumpBoost() {
		return this.jumpBoost;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.entityId = in.readVarInt();
		this.state = MagicValues.key(PlayerState.class, in.readUnsignedByte());
		this.jumpBoost = in.readVarInt();
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeVarInt(this.entityId);
		out.writeByte(MagicValues.value(Integer.class, this.state));
		out.writeVarInt(this.jumpBoost);
	}

	@Override
	public boolean isPriority() {
		return false;
	}

}
