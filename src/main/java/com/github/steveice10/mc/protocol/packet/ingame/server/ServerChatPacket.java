package com.github.steveice10.mc.protocol.packet.ingame.server;

import com.github.steveice10.mc.protocol.data.DefaultComponentSerializer;
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
import lombok.With;
import net.kyori.adventure.text.Component;

import java.io.IOException;
import java.util.UUID;

@Data
@With
@Setter(AccessLevel.NONE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ServerChatPacket implements Packet {
    private @NonNull Component message;
    private @NonNull MessageType type;
    private @NonNull UUID senderUuid;

    public ServerChatPacket(@NonNull String text) {
        this(DefaultComponentSerializer.get().deserialize(text));
    }

    public ServerChatPacket(@NonNull Component message) {
        this(message, MessageType.SYSTEM);
    }

    public ServerChatPacket(@NonNull String text, @NonNull MessageType type) {
        this(DefaultComponentSerializer.get().deserialize(text), type, new UUID(0, 0));
    }

    public ServerChatPacket(@NonNull Component message, @NonNull MessageType type) {
        this(message, type, new UUID(0, 0));
    }

    public ServerChatPacket(@NonNull String text, @NonNull MessageType type, @NonNull UUID uuid) {
        this(DefaultComponentSerializer.get().deserialize(text), type, uuid);
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.message = DefaultComponentSerializer.get().deserialize(in.readString());
        this.type = MagicValues.key(MessageType.class, in.readByte());
        this.senderUuid = in.readUUID();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeString(DefaultComponentSerializer.get().serialize(this.message));
        out.writeByte(MagicValues.value(Integer.class, this.type));
        out.writeUUID(this.senderUuid);
    }

    @Override
    public boolean isPriority() {
        return false;
    }
}
