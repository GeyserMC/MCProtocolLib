package com.github.steveice10.mc.protocol.packet.ingame.clientbound;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import com.github.steveice10.mc.protocol.data.DefaultComponentSerializer;
import com.github.steveice10.mc.protocol.data.game.BuiltinChatType;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import lombok.With;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.UUID;

@Data
@With
@AllArgsConstructor
public class ClientboundPlayerChatPacket implements MinecraftPacket {
	private final Component signedContent;
	private final @Nullable Component unsignedContent;
	/**
	 * Is {@link BuiltinChatType} defined in the order sent by the server in the login packet.
	 */
	private final int typeId;
	private final UUID senderUUID;
	private final Component senderName;
	private final @Nullable Component targetName;
	private final long salt;
	@ToString.Exclude
	private final byte[] signature;
	private final long timeStamp;

	public ClientboundPlayerChatPacket(ByteBuf in, MinecraftCodecHelper helper) {
		this.signedContent = helper.readComponent(in);
		if (in.readBoolean()) {
			this.unsignedContent = helper.readComponent(in);
		} else {
			this.unsignedContent = null;
		}

		this.typeId = helper.readVarInt(in);
		this.senderUUID = helper.readUUID(in);
		this.senderName = helper.readComponent(in);
		if (in.readBoolean()) {
			this.targetName = helper.readComponent(in);
		} else {
			this.targetName = null;
		}

		this.timeStamp = in.readLong();
		this.salt = in.readLong();
		this.signature = helper.readByteArray(in);
	}

	@Override
	public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
		helper.writeComponent(out, this.signedContent);
		if (this.unsignedContent != null) {
			helper.writeComponent(out, this.unsignedContent);
		}

		helper.writeVarInt(out, this.typeId);
		helper.writeUUID(out, this.senderUUID);
		DefaultComponentSerializer.get().serialize(this.senderName);
		if (this.targetName != null) {
			DefaultComponentSerializer.get().serialize(this.targetName);
		}

		out.writeLong(this.timeStamp);
		out.writeLong(this.salt);
		helper.writeVarInt(out, this.signature.length);
		out.writeBytes(this.signature);
	}
}
