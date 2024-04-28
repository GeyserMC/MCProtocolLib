package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
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

    public ClientboundPlayerChatPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.sender = helper.readUUID(in);
        this.index = helper.readVarInt(in);
        if (in.readBoolean()) {
            this.messageSignature = new byte[256];
            in.readBytes(this.messageSignature);
        } else {
            this.messageSignature = null;
        }

        this.content = helper.readString(in, 256);
        this.timeStamp = in.readLong();
        this.salt = in.readLong();

        this.lastSeenMessages = new ArrayList<>();
        int seenMessageCount = Math.min(helper.readVarInt(in), 20);
        for (int i = 0; i < seenMessageCount; i++) {
            this.lastSeenMessages.add(MessageSignature.read(in, helper));
        }

        this.unsignedContent = helper.readNullable(in, helper::readComponent);
        this.filterMask = ChatFilterType.from(helper.readVarInt(in));
        this.chatType = helper.readVarInt(in);
        this.name = helper.readComponent(in);
        this.targetName = helper.readNullable(in, helper::readComponent);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writeUUID(out, this.sender);
        helper.writeVarInt(out, this.index);
        out.writeBoolean(this.messageSignature != null);
        if (this.messageSignature != null) {
            out.writeBytes(this.messageSignature);
        }

        helper.writeString(out, this.content);
        out.writeLong(this.timeStamp);
        out.writeLong(this.salt);

        helper.writeVarInt(out, this.lastSeenMessages.size());
        for (MessageSignature messageSignature : this.lastSeenMessages) {
            helper.writeVarInt(out, messageSignature.getId() + 1);
            if (messageSignature.getMessageSignature() != null) {
                out.writeBytes(messageSignature.getMessageSignature());
            }
        }

        helper.writeNullable(out, this.unsignedContent, helper::writeComponent);
        helper.writeVarInt(out, this.filterMask.ordinal());
        helper.writeVarInt(out, this.chatType);
        helper.writeComponent(out, this.name);
        helper.writeNullable(out, this.targetName, helper::writeComponent);
    }
}
