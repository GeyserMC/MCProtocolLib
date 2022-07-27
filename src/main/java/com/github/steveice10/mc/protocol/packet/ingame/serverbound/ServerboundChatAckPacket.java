package com.github.steveice10.mc.protocol.packet.ingame.serverbound;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import com.github.steveice10.mc.protocol.data.game.LastSeenMessage;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Data
@With
@AllArgsConstructor
public class ServerboundChatAckPacket implements MinecraftPacket {
	private final List<LastSeenMessage> lastSeenMessages;
	private final @Nullable LastSeenMessage lastReceivedMessage;

	public ServerboundChatAckPacket(ByteBuf in, MinecraftCodecHelper helper) throws IOException {
		this.lastSeenMessages = new ArrayList<>();
		int seenMessageCount = Math.min(helper.readVarInt(in), 5);
		for (int i = 0; i < seenMessageCount; i++) {
			lastSeenMessages.add(new LastSeenMessage(helper.readUUID(in), helper.readByteArray(in)));
		}

		if (in.readBoolean()) {
			this.lastReceivedMessage = new LastSeenMessage(helper.readUUID(in), helper.readByteArray(in));
		} else {
			this.lastReceivedMessage = null;
		}
	}

	@Override
	public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
		helper.writeVarInt(out, this.lastSeenMessages.size());
		for (LastSeenMessage entry : this.lastSeenMessages) {
			helper.writeUUID(out, entry.getProfileId());
			helper.writeVarInt(out, entry.getLastSignature().length);
			out.writeBytes(entry.getLastSignature());
		}

		if (this.lastReceivedMessage != null) {
			out.writeBoolean(true);
			helper.writeUUID(out, this.lastReceivedMessage.getProfileId());
			helper.writeVarInt(out, this.lastReceivedMessage.getLastSignature().length);
			out.writeBytes(this.lastReceivedMessage.getLastSignature());
		} else {
			out.writeBoolean(false);
		}
	}
}
