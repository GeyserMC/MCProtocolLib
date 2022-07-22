package com.github.steveice10.mc.protocol.packet.ingame.clientbound;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.UUID;

@Data
@With
@AllArgsConstructor
public class ClientboundPlayerChatHeaderPacket implements MinecraftPacket {
	private final @Nullable byte[] previousSignature;
	private final UUID sender;
	private final byte[] headerSignature;
	private final byte[] bodyDigest;

	public ClientboundPlayerChatHeaderPacket(ByteBuf in, MinecraftCodecHelper helper) throws IOException {
		if (in.readBoolean()) {
			this.previousSignature = helper.readByteArray(in);
		} else {
			this.previousSignature = null;
		}

		this.sender = helper.readUUID(in);
		this.headerSignature = helper.readByteArray(in);
		this.bodyDigest = helper.readByteArray(in);
	}

	@Override
	public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
		if (this.previousSignature != null) {
			out.writeBoolean(true);
			helper.writeVarInt(out, previousSignature.length);
			out.writeBytes(this.previousSignature);
		} else {
			out.writeBoolean(false);
		}

		helper.writeUUID(out, this.sender);
		helper.writeVarInt(out, this.headerSignature.length);
		out.writeBytes(this.headerSignature);
		helper.writeVarInt(out, this.bodyDigest.length);
		out.writeBytes(this.bodyDigest);
	}
}
