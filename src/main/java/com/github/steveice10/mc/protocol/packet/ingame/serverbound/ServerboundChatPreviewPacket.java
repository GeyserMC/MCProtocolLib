package com.github.steveice10.mc.protocol.packet.ingame.serverbound;

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
public class ServerboundChatPreviewPacket implements Packet {
	private final int queryId;
	private final String query;

	public ServerboundChatPreviewPacket(NetInput in) throws IOException {
		this.queryId = in.readInt();
		this.query = in.readString();
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeInt(this.queryId);
		out.writeString(this.query);
	}
}
