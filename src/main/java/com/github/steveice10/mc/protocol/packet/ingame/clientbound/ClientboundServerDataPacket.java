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
public class ClientboundServerDataPacket implements Packet {
	private final @Nullable Component motd;
	private final @Nullable String iconBase64;
	private final boolean previewsChat;

	public ClientboundServerDataPacket(NetInput in) throws IOException {
		if (in.readBoolean()) {
			this.motd = DefaultComponentSerializer.get().deserialize(in.readString());
		} else {
			this.motd = null;
		}

		if (in.readBoolean()) {
			this.iconBase64 = in.readString();
		} else {
			this.iconBase64 = null;
		}

		this.previewsChat = in.readBoolean();
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeBoolean(this.motd != null);
		if (this.motd != null) {
			out.writeString(DefaultComponentSerializer.get().serialize(this.motd));
		}

		out.writeBoolean(this.iconBase64 != null);
		if (this.iconBase64 != null) {
			out.writeString(this.iconBase64);
		}

		out.writeBoolean(this.previewsChat);
	}
}
