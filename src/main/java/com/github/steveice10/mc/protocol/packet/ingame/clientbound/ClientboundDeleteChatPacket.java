package com.github.steveice10.mc.protocol.packet.ingame.clientbound;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import com.github.steveice10.mc.protocol.data.game.chat.MessageSignature;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;

import java.io.IOException;

@Data
@With
@AllArgsConstructor
public class ClientboundDeleteChatPacket implements MinecraftPacket {
	private final MessageSignature messageSignature;

	public ClientboundDeleteChatPacket(ByteBuf in, MinecraftCodecHelper helper) throws IOException {
		int id = helper.readVarInt(in) - 1;
		this.messageSignature = new MessageSignature(id, id == -1 ? in.readBytes(new byte[256]).array() : null);
	}

	@Override
	public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
		helper.writeVarInt(out, this.messageSignature.getId() + 1);
		if (this.messageSignature.getMessageSignature() != null) {
			out.writeBytes(messageSignature.getMessageSignature());
		}
	}
}
