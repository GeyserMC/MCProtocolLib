package com.github.steveice10.mc.protocol.packet.ingame.server;

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.MessageType;
import com.github.steveice10.mc.protocol.data.message.Message;
import com.github.steveice10.mc.protocol.data.message.MessageSerializer;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.IOException;
import java.util.UUID;

@Data
@Setter(AccessLevel.NONE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@AllArgsConstructor
public class ServerChatPacket implements Packet {
    private @NonNull Message message;
    private @NonNull MessageType type;
    private @NonNull UUID senderUuid;

    private String rawMessage;

    public ServerChatPacket(@NonNull String text) {
        this(MessageSerializer.fromString(text));
    }

    public ServerChatPacket(@NonNull Message message) {
        this(message, MessageType.SYSTEM);
    }

    public ServerChatPacket(@NonNull String text, @NonNull MessageType type) {
        this(MessageSerializer.fromString(text), type, new UUID(0, 0));
    }

    public ServerChatPacket(@NonNull Message message, @NonNull MessageType type) {
        this(message, type, new UUID(0, 0));
    }

    public ServerChatPacket(@NonNull String text, @NonNull MessageType type, @NonNull UUID uuid) {
        this(MessageSerializer.fromString(text), type, uuid);
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.rawMessage = in.readString();
        this.message = MessageSerializer.fromString(this.rawMessage);
        this.type = MagicValues.key(MessageType.class, in.readByte());
        this.senderUuid = in.readUUID();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeString(MessageSerializer.toJsonString(this.message));
        out.writeByte(MagicValues.value(Integer.class, this.type));
        out.writeUUID(this.senderUuid);
    }

    @Override
    public boolean isPriority() {
        return false;
    }
}
