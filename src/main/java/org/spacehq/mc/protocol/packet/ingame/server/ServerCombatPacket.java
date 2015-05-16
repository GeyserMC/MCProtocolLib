package org.spacehq.mc.protocol.packet.ingame.server;

import org.spacehq.mc.protocol.data.game.values.MagicValues;
import org.spacehq.mc.protocol.data.game.values.entity.player.CombatState;
import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.io.NetOutput;
import org.spacehq.packetlib.packet.Packet;

import java.io.IOException;

public class ServerCombatPacket implements Packet {

	private CombatState state;
	private int entityId;
	private int duration;
	private int playerId;
	private String message;

	public ServerCombatPacket() {
		this.state = CombatState.ENTER_COMBAT;
	}

	public ServerCombatPacket(int entityId, int duration) {
		this.state = CombatState.END_COMBAT;
		this.entityId = entityId;
		this.duration = duration;
	}

	public ServerCombatPacket(int entityId, int playerId, String message) {
		this.state = CombatState.ENTITY_DEAD;
		this.entityId = entityId;
		this.playerId = playerId;
		this.message = message;
	}

	public CombatState getCombatState() {
		return this.state;
	}

	public int getEntityId() {
		return this.entityId;
	}

	public int getDuration() {
		return this.duration;
	}

	public int getPlayerId() {
		return this.playerId;
	}

	public String getMessage() {
		return this.message;
	}

	public void read(NetInput in) throws IOException {
		this.state = MagicValues.key(CombatState.class, in.readVarInt());
		if(this.state == CombatState.END_COMBAT) {
			this.duration = in.readVarInt();
			this.entityId = in.readInt();
		} else if(this.state == CombatState.ENTITY_DEAD) {
			this.playerId = in.readVarInt();
			this.entityId = in.readInt();
			this.message = in.readString();
		}
	}

	public void write(NetOutput out) throws IOException {
		out.writeVarInt(MagicValues.value(Integer.class, this.state));
		if(this.state == CombatState.END_COMBAT) {
			out.writeVarInt(this.duration);
			out.writeInt(this.entityId);
		} else if(this.state == CombatState.ENTITY_DEAD) {
			out.writeVarInt(this.playerId);
			out.writeInt(this.entityId);
			out.writeString(this.message);
		}
	}

	public boolean isPriority() {
		return false;
	}

}
