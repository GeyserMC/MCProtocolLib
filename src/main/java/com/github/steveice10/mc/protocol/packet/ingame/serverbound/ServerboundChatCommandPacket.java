package com.github.steveice10.mc.protocol.packet.ingame.serverbound;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Data
@With
@AllArgsConstructor
public class ServerboundChatCommandPacket implements MinecraftPacket {
	private final String command;
	private final long timeStamp;
	private final long salt;
	private final Map<String, byte[]> signatures;
	private final boolean signedPreview;

	public ServerboundChatCommandPacket(ByteBuf in, MinecraftCodecHelper helper) throws IOException {
		this.command = helper.readString(in);
		this.timeStamp = in.readLong();
		this.salt = in.readLong();
		this.signatures = new HashMap<>();
		int signatureCount = helper.readVarInt(in);
		for (int i = 0; i < signatureCount; i++) {
			String signatureId = helper.readString(in);
			byte[] signature = helper.readByteArray(in);
			signatures.put(signatureId, signature);
		}

		this.signedPreview = in.readBoolean();
	}

	@Override
	public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
		helper.writeString(out, this.command);
		out.writeLong(this.timeStamp);
		out.writeLong(this.salt);
		helper.writeVarInt(out, this.signatures.size());
		for (Map.Entry<String, byte[]> signature : this.signatures.entrySet()) {
			helper.writeString(out, signature.getKey());
			helper.writeVarInt(out, signature.getValue().length);
			out.writeBytes(signature.getValue());
		}

		out.writeBoolean(this.signedPreview);
	}
}
