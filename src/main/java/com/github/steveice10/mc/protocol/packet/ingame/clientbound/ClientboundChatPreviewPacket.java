package com.github.steveice10.mc.protocol.packet.ingame.clientbound;

import com.github.steveice10.mc.protocol.data.DefaultComponentSerializer;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import net.kyori.adventure.text.Component;

import javax.annotation.Nullable;
import java.io.IOException;

@Data
@With
@AllArgsConstructor
public class ClientboundChatPreviewPacket implements Packet {
	private final int queryId;
	private final @Nullable Component preview;

	public ClientboundChatPreviewPacket(NetInput in) throws IOException {
		this.queryId = in.readInt();
		if (in.readBoolean()) {
			this.preview = DefaultComponentSerializer.get().deserialize(in.readString());
		} else {
			this.preview = null;
		}
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeInt(this.queryId);
		if (this.preview != null) {
			out.writeString(DefaultComponentSerializer.get().serialize(this.preview));
		}
	}
}
