package com.github.steveice10.mc.protocol.packet.ingame.clientbound;

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
public class ClientboundDeleteChatPacket implements MinecraftPacket {
	private final byte[] messageSignature;

	public ClientboundDeleteChatPacket(ByteBuf in, MinecraftCodecHelper helper) throws IOException {
		this.messageSignature = helper.readByteArray(in);
	}

	@Override
	public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
		helper.writeVarInt(out, this.messageSignature.length);
		out.writeBytes(this.messageSignature);
	}
}
