package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;
import org.geysermc.mcprotocollib.protocol.data.game.Holder;
import org.geysermc.mcprotocollib.protocol.data.game.chat.ChatFilterType;
import org.geysermc.mcprotocollib.protocol.data.game.chat.ChatType;
import org.geysermc.mcprotocollib.protocol.data.game.chat.MessageSignature;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@With
@AllArgsConstructor
public class ClientboundPlayerChatPacket implements MinecraftPacket {
    private final int globalIndex;
    private final UUID sender;
    private final int index;
    private final byte @Nullable [] messageSignature;
    private final String content;
    private final long timeStamp;
    private final long salt;
    private final List<MessageSignature> lastSeenMessages;
    private final @Nullable Component unsignedContent;
    private final ChatFilterType filterMask;
    private final Holder<ChatType> chatType;
    private final Component name;
    private final @Nullable Component targetName;

    public ClientboundPlayerChatPacket(ByteBuf in) {
        this.globalIndex = MinecraftTypes.readVarInt(in);
        this.sender = MinecraftTypes.readUUID(in);
        this.index = MinecraftTypes.readVarInt(in);
        this.messageSignature = MinecraftTypes.readNullable(in, buf -> {
            byte[] signature = new byte[256];
            buf.readBytes(signature);
            return signature;
        });

        this.content = MinecraftTypes.readString(in, 256);
        this.timeStamp = in.readLong();
        this.salt = in.readLong();

        this.lastSeenMessages = new ArrayList<>();
        int seenMessageCount = Math.min(MinecraftTypes.readVarInt(in), 20);
        for (int i = 0; i < seenMessageCount; i++) {
            this.lastSeenMessages.add(MessageSignature.read(in));
        }

        this.unsignedContent = MinecraftTypes.readNullable(in, MinecraftTypes::readComponent);
        this.filterMask = ChatFilterType.from(MinecraftTypes.readVarInt(in));
        this.chatType = MinecraftTypes.readHolder(in, MinecraftTypes::readChatType);
        this.name = MinecraftTypes.readComponent(in);
        this.targetName = MinecraftTypes.readNullable(in, MinecraftTypes::readComponent);
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeVarInt(out, this.globalIndex);
        MinecraftTypes.writeUUID(out, this.sender);
        MinecraftTypes.writeVarInt(out, this.index);
        MinecraftTypes.writeNullable(out, this.messageSignature, ByteBuf::writeBytes);

        MinecraftTypes.writeString(out, this.content);
        out.writeLong(this.timeStamp);
        out.writeLong(this.salt);

        MinecraftTypes.writeVarInt(out, this.lastSeenMessages.size());
        for (MessageSignature messageSignature : this.lastSeenMessages) {
            MinecraftTypes.writeVarInt(out, messageSignature.getId() + 1);
            if (messageSignature.getMessageSignature() != null) {
                out.writeBytes(messageSignature.getMessageSignature());
            }
        }

        MinecraftTypes.writeNullable(out, this.unsignedContent, MinecraftTypes::writeComponent);
        MinecraftTypes.writeVarInt(out, this.filterMask.ordinal());
        MinecraftTypes.writeHolder(out, this.chatType, MinecraftTypes::writeChatType);
        MinecraftTypes.writeComponent(out, this.name);
        MinecraftTypes.writeNullable(out, this.targetName, MinecraftTypes::writeComponent);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
