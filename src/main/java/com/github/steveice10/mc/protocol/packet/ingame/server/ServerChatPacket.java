package com.github.steveice10.mc.protocol.packet.ingame.server;

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.MessageType;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;

import java.io.IOException;
import java.util.UUID;

@Data
@Setter(AccessLevel.NONE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ServerChatPacket implements Packet {
    private @NonNull Component message;
    private @NonNull MessageType type;
    private @NonNull UUID senderUuid;

    public ServerChatPacket(@NonNull String text) {
        this(GsonComponentSerializer.gson().deserialize(text));
    }

    public ServerChatPacket(@NonNull Component message) {
        this(message, MessageType.SYSTEM);
    }

    public ServerChatPacket(@NonNull String text, @NonNull MessageType type) {
        this(GsonComponentSerializer.gson().deserialize(text), type, new UUID(0, 0));
    }

    public ServerChatPacket(@NonNull Component message, @NonNull MessageType type) {
        this(message, type, new UUID(0, 0));
    }

    public ServerChatPacket(@NonNull String text, @NonNull MessageType type, @NonNull UUID uuid) {
        this(GsonComponentSerializer.gson().deserialize(text), type, uuid);
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.message = GsonComponentSerializer.gson().deserialize(in.readString());
        this.type = MagicValues.key(MessageType.class, in.readByte());
        this.senderUuid = in.readUUID();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeString(GsonComponentSerializer.gson().serialize(this.message));
        out.writeByte(MagicValues.value(Integer.class, this.type));
        out.writeUUID(this.senderUuid);
    }

    @Override
    public boolean isPriority() {
        return false;
    }
}
