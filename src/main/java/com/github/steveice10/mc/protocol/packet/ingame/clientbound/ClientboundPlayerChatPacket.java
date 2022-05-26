package com.github.steveice10.mc.protocol.packet.ingame.clientbound;

import com.github.steveice10.mc.protocol.data.DefaultComponentSerializer;
import com.github.steveice10.mc.protocol.data.game.MessageType;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import lombok.With;
import net.kyori.adventure.text.Component;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.UUID;

@Data
@With
@AllArgsConstructor
public class ClientboundPlayerChatPacket implements Packet {
	private final Component signedContent;
	private final @Nullable Component unsignedContent;
	private final MessageType type;
	private final UUID senderUUID;
	private final Component senderName;
	private final @Nullable Component senderTeamName;
	private final long salt;
	@ToString.Exclude
	private final byte[] signature;
	private final long timeStamp;

	public ClientboundPlayerChatPacket(NetInput in) throws IOException {
		this.signedContent = DefaultComponentSerializer.get().deserialize(in.readString());
		if (in.readBoolean()) {
			this.unsignedContent = DefaultComponentSerializer.get().deserialize(in.readString());
		} else {
			this.unsignedContent = null;
		}

		this.type = in.readEnum(MessageType.VALUES);
		this.senderUUID = in.readUUID();
		this.senderName = DefaultComponentSerializer.get().deserialize(in.readString());
		if (in.readBoolean()) {
			this.senderTeamName = DefaultComponentSerializer.get().deserialize(in.readString());
		} else {
			this.senderTeamName = null;
		}

		this.timeStamp = in.readLong();
		this.salt = in.readLong();
		this.signature = in.readBytes(in.readVarInt());
	}

	@Override
	public void write(NetOutput out) throws IOException {
		DefaultComponentSerializer.get().serialize(this.signedContent);
		if (this.unsignedContent != null) {
			DefaultComponentSerializer.get().serialize(this.unsignedContent);
		}

		out.writeEnum(this.type);
		out.writeUUID(this.senderUUID);
		DefaultComponentSerializer.get().serialize(this.senderName);
		if (this.senderTeamName != null) {
			DefaultComponentSerializer.get().serialize(this.senderTeamName);
		}

		out.writeLong(this.timeStamp);
		out.writeLong(this.salt);
		out.writeVarInt(this.signature.length);
		out.writeBytes(this.signature);
	}
}
