package com.github.steveice10.mc.protocol.packet.ingame.serverbound;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;

import java.io.IOException;

@Data
@With
@AllArgsConstructor
public class ServerboundChatPreviewPacket implements MinecraftPacket {
	private final int queryId;
	private final String query;

	public ServerboundChatPreviewPacket(ByteBuf in, MinecraftCodecHelper helper) throws IOException {
		this.queryId = in.readInt();
		this.query = helper.readString(in);
	}

	@Override
	public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
		out.writeInt(this.queryId);
		helper.writeString(out, this.query);
	}
}
