package com.github.steveice10.mc.protocol.packet.ingame.serverbound;

import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Data
@With
@AllArgsConstructor
public class ServerboundChatCommandPacket implements Packet {
	private final String command;
	private final long timeStamp;
	private final long salt;
	private final Map<String, byte[]> signatures;
	private final boolean signedPreview;

	public ServerboundChatCommandPacket(NetInput in) throws IOException {
		this.command = in.readString();
		this.timeStamp = in.readLong();
		this.salt = in.readLong();
		this.signatures = new HashMap<>();
		int signatureCount = in.readVarInt();
		for (int i = 0; i < signatureCount; i++) {
			String signatureId = in.readString();
			byte[] signature = in.readBytes(in.readVarInt());
			signatures.put(signatureId, signature);
		}

		this.signedPreview = in.readBoolean();
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeString(this.command);
		out.writeLong(this.timeStamp);
		out.writeLong(this.salt);
		out.writeVarInt(this.signatures.size());
		for (Map.Entry<String, byte[]> signature : this.signatures.entrySet()) {
			out.writeString(signature.getKey());
			out.writeVarInt(signature.getValue().length);
			out.writeBytes(signature.getValue());
		}

		out.writeBoolean(this.signedPreview);
	}
}
