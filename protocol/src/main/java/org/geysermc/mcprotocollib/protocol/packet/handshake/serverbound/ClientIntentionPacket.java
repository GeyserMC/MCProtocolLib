package org.geysermc.mcprotocollib.protocol.packet.handshake.serverbound;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.data.handshake.HandshakeIntent;

@Data
@With
@AllArgsConstructor
public class ClientIntentionPacket implements MinecraftPacket {
    private final int protocolVersion;
    private final @NonNull String hostname;
    private final int port;
    private final @NonNull HandshakeIntent intent;

    public ClientIntentionPacket(MinecraftByteBuf buf) {
        this.protocolVersion = buf.readVarInt();
        this.hostname = buf.readString();
        this.port = buf.readUnsignedShort();
        this.intent = HandshakeIntent.from(buf.readVarInt() - 1);
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeVarInt(this.protocolVersion);
        buf.writeString(this.hostname);
        buf.writeShort(this.port);
        buf.writeVarInt(this.intent.ordinal() + 1);
    }

    @Override
    public boolean isPriority() {
        return true;
    }
}
