package com.github.steveice10.mc.protocol.packet.ingame.clientbound;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import com.github.steveice10.mc.protocol.data.DefaultComponentSerializer;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

@Data
@With
@AllArgsConstructor
public class ClientboundServerDataPacket implements MinecraftPacket {
	private final @Nullable Component motd;
	private final @Nullable String iconBase64;
	private final boolean previewsChat;

	public ClientboundServerDataPacket(ByteBuf in, MinecraftCodecHelper helper) throws IOException {
		if (in.readBoolean()) {
			this.motd = helper.readComponent(in);
		} else {
			this.motd = null;
		}

		if (in.readBoolean()) {
			this.iconBase64 = helper.readString(in);
		} else {
			this.iconBase64 = null;
		}

		this.previewsChat = in.readBoolean();
	}

	@Override
	public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
		out.writeBoolean(this.motd != null);
		if (this.motd != null) {
			helper.writeString(out, DefaultComponentSerializer.get().serialize(this.motd));
		}

		out.writeBoolean(this.iconBase64 != null);
		if (this.iconBase64 != null) {
			helper.writeString(out, this.iconBase64);
		}

		out.writeBoolean(this.previewsChat);
	}
}
