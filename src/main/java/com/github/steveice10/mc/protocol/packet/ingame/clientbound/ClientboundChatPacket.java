package com.github.steveice10.mc.protocol.packet.ingame.clientbound;

import com.github.steveice10.mc.protocol.data.DefaultComponentSerializer;
import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.MessageType;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import net.kyori.adventure.text.Component;

import java.io.IOException;
import java.util.UUID;

@Data
@With
@AllArgsConstructor
public class ClientboundChatPacket implements Packet {
    private final @NonNull Component message;
    private final @NonNull MessageType type;
    private final @NonNull UUID senderUuid;

    public ClientboundChatPacket(@NonNull String text) {
        this(DefaultComponentSerializer.get().deserialize(text));
    }

    public ClientboundChatPacket(@NonNull Component message) {
        this(message, MessageType.SYSTEM);
    }

    public ClientboundChatPacket(@NonNull String text, @NonNull MessageType type) {
        this(DefaultComponentSerializer.get().deserialize(text), type, new UUID(0, 0));
    }

    public ClientboundChatPacket(@NonNull Component message, @NonNull MessageType type) {
        this(message, type, new UUID(0, 0));
    }

    public ClientboundChatPacket(@NonNull String text, @NonNull MessageType type, @NonNull UUID uuid) {
        this(DefaultComponentSerializer.get().deserialize(text), type, uuid);
    }

    public ClientboundChatPacket(NetInput in) throws IOException {
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
}
