package com.github.steveice10.mc.protocol.packet.ingame.clientbound;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import com.github.steveice10.mc.protocol.data.game.chat.BuiltinChatType;
import com.github.steveice10.mc.protocol.data.game.chat.ChatFilterType;
import com.github.steveice10.mc.protocol.data.game.chat.MessageSignature;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@With
@AllArgsConstructor
public class ClientboundPlayerChatPacket implements MinecraftPacket {
    private final UUID sender;
    private final int index;
    private final byte @Nullable[] messageSignature;
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
            this.messageSignature = in.readBytes(new byte[256]).array();
        } else {
            this.messageSignature = null;
        }

        this.content = helper.readString(in, 256);
        this.timeStamp = in.readLong();
        this.salt = in.readLong();

        this.lastSeenMessages = new ArrayList<>();
        int seenMessageCount = Math.min(helper.readVarInt(in), 20);
        for (int i = 0; i < seenMessageCount; i++) {
            int id = helper.readVarInt(in) - 1;
            byte[] messageSignature = id == -1 ? in.readBytes(new byte[256]).array() : null;
            this.lastSeenMessages.add(new MessageSignature(id, messageSignature));
        }

        this.unsignedContent = helper.readNullable(in, helper::readComponent);
        this.filterMask = ChatFilterType.from(helper.readVarInt(in));
        this.chatType = helper.readVarInt(in);
        this.name = helper.readComponent(in);
        this.targetName = helper.readNullable(in, helper::readComponent);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
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
