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
public class ClientboundChatPreviewPacket implements MinecraftPacket {
	private final int queryId;
	private final @Nullable Component preview;

	public ClientboundChatPreviewPacket(ByteBuf in, MinecraftCodecHelper helper) throws IOException {
		this.queryId = in.readInt();
		if (in.readBoolean()) {
			this.preview = helper.readComponent(in);
		} else {
			this.preview = null;
		}
	}

	@Override
	public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
		out.writeInt(this.queryId);
		out.writeBoolean(this.preview != null);
		if (this.preview != null) {
			helper.writeString(out, DefaultComponentSerializer.get().serialize(this.preview));
		}
	}
}
