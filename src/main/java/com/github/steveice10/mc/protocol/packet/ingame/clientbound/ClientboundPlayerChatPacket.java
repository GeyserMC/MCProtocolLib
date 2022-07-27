package com.github.steveice10.mc.protocol.packet.ingame.clientbound;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import com.github.steveice10.mc.protocol.data.game.BuiltinChatType;
import com.github.steveice10.mc.protocol.data.game.ChatFilterType;
import com.github.steveice10.mc.protocol.data.game.LastSeenMessage;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.UUID;

@Data
@With
@AllArgsConstructor
public class ClientboundPlayerChatPacket implements MinecraftPacket {
	private final byte @Nullable[] previousSignature;
	private final UUID sender;
	private final byte[] headerSignature;
	private final String messagePlain;
	private final Component messageDecorated;
	private final long timeStamp;
	private final long salt;
	private final List<LastSeenMessage> lastSeenMessages;
	private final @Nullable Component unsignedContent;
	private final BitSet filterMask;
	private final ChatFilterType filterType;
	/**
	 * Is {@link BuiltinChatType} defined in the order sent by the server in the login packet.
	 */
	private final int chatType;
	private final Component name;
	private final @Nullable Component targetName;

	public ClientboundPlayerChatPacket(ByteBuf in, MinecraftCodecHelper helper) {
		if (in.readBoolean()) {
			this.previousSignature = helper.readByteArray(in);
		} else {
			this.previousSignature = null;
		}

		this.sender = helper.readUUID(in);
		this.headerSignature = helper.readByteArray(in);
		this.messagePlain = helper.readString(in);
		if (in.readBoolean()) {
			this.messageDecorated = helper.readComponent(in);
		} else {
			this.messageDecorated = Component.text(this.messagePlain);
		}

		this.timeStamp = in.readLong();
		this.salt = in.readLong();
		this.lastSeenMessages = new ArrayList<>();
		int seenMessageCount = Math.min(helper.readVarInt(in), 5);
		for (int i = 0; i < seenMessageCount; i++) {
			lastSeenMessages.add(new LastSeenMessage(helper.readUUID(in), helper.readByteArray(in)));
		}

		if (in.readBoolean()) {
			this.unsignedContent = helper.readComponent(in);
		} else {
			this.unsignedContent = null;
		}

		this.filterType = ChatFilterType.from(helper.readVarInt(in));
		if (filterType == ChatFilterType.PARTIALLY_FILTERED) {
			this.filterMask = BitSet.valueOf(helper.readLongArray(in));
		} else {
			this.filterMask = new BitSet(0);
		}

		this.chatType = helper.readVarInt(in);
		this.name = helper.readComponent(in);
		if (in.readBoolean()) {
			this.targetName = helper.readComponent(in);
		} else {
			this.targetName = null;
		}
	}

	@Override
	public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
		if (this.previousSignature != null) {
			out.writeBoolean(true);
			helper.writeVarInt(out, this.previousSignature.length);
			out.writeBytes(this.previousSignature);
		} else {
			out.writeBoolean(false);
		}

		helper.writeUUID(out, this.sender);
		helper.writeVarInt(out, this.headerSignature.length);
		out.writeBytes(this.headerSignature);
		helper.writeString(out, this.messagePlain);
		if (!this.messageDecorated.equals(Component.text(this.messagePlain))) {
			out.writeBoolean(true);
			helper.writeComponent(out, this.messageDecorated);
		} else {
			out.writeBoolean(false);
		}

		out.writeLong(this.timeStamp);
		out.writeLong(this.salt);
		helper.writeVarInt(out, this.lastSeenMessages.size());
		for (LastSeenMessage entry : this.lastSeenMessages) {
			helper.writeUUID(out, entry.getProfileId());
			helper.writeVarInt(out, entry.getLastSignature().length);
			out.writeBytes(entry.getLastSignature());
		}

		if (this.unsignedContent != null) {
			out.writeBoolean(true);
			helper.writeComponent(out, this.unsignedContent);
		} else {
			out.writeBoolean(false);
		}

		helper.writeVarInt(out, this.filterType.ordinal());
		if (this.filterType == ChatFilterType.PARTIALLY_FILTERED) {
			helper.writeLongArray(out, this.filterMask.toLongArray());
		}

		helper.writeVarInt(out, this.chatType);
		helper.writeComponent(out, this.name);
		if (this.targetName != null) {
			out.writeBoolean(true);
			helper.writeComponent(out, this.targetName);
		} else {
			out.writeBoolean(false);
		}
	}
}
