package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.data.game.chat.BuiltinChatType;
import org.geysermc.mcprotocollib.protocol.data.game.chat.ChatFilterType;
import org.geysermc.mcprotocollib.protocol.data.game.chat.MessageSignature;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@With
@AllArgsConstructor
public class ClientboundPlayerChatPacket implements MinecraftPacket {
    private final UUID sender;
    private final int index;
    private final byte @Nullable [] messageSignature;
    private final String content;
    private final long timeStamp;
    private final long salt;
    private final List<MessageSignature> lastSeenMessages;
    private final @Nullable Component unsignedContent;
    private final ChatFilterType filterMask;
    /**
     * Is {@link BuiltinChatType} defined in the order sent by the server in the login packet.
     */
    private final int chatType;
    private final Component name;
    private final @Nullable Component targetName;

    public ClientboundPlayerChatPacket(MinecraftByteBuf buf) {
        this.sender = buf.readUUID();
        this.index = buf.readVarInt();
        if (buf.readBoolean()) {
            this.messageSignature = new byte[256];
            buf.readBytes(this.messageSignature);
        } else {
            this.messageSignature = null;
        }

        this.content = buf.readString(256);
        this.timeStamp = buf.readLong();
        this.salt = buf.readLong();

        this.lastSeenMessages = new ArrayList<>();
        int seenMessageCount = Math.min(buf.readVarInt(), 20);
        for (int i = 0; i < seenMessageCount; i++) {
            this.lastSeenMessages.add(MessageSignature.read(buf));
        }

        this.unsignedContent = buf.readNullable(buf::readComponent);
        this.filterMask = ChatFilterType.from(buf.readVarInt());
        this.chatType = buf.readVarInt();
        this.name = buf.readComponent();
        this.targetName = buf.readNullable(buf::readComponent);
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeUUID(this.sender);
        buf.writeVarInt(this.index);
        buf.writeBoolean(this.messageSignature != null);
        if (this.messageSignature != null) {
            buf.writeBytes(this.messageSignature);
        }

        buf.writeString(this.content);
        buf.writeLong(this.timeStamp);
        buf.writeLong(this.salt);

        buf.writeVarInt(this.lastSeenMessages.size());
        for (MessageSignature messageSignature : this.lastSeenMessages) {
            buf.writeVarInt(messageSignature.getId() + 1);
            if (messageSignature.getMessageSignature() != null) {
                buf.writeBytes(messageSignature.getMessageSignature());
            }
        }

        buf.writeNullable(this.unsignedContent, buf::writeComponent);
        buf.writeVarInt(this.filterMask.ordinal());
        buf.writeVarInt(this.chatType);
        buf.writeComponent(this.name);
        buf.writeNullable(this.targetName, buf::writeComponent);
    }
}
