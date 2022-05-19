package com.github.steveice10.mc.protocol.packet.ingame.clientbound.entity.player;

import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;

import java.io.IOException;

@Data
@With
@AllArgsConstructor
public class ClientboundBlockChangedAckPacket implements Packet {
	private final int sequence;

	public ClientboundBlockChangedAckPacket(NetInput in) throws IOException {
		this.sequence = in.readVarInt();
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeVarInt(this.sequence);
	}
}
