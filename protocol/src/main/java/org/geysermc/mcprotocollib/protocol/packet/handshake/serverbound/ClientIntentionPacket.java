package org.geysermc.mcprotocollib.protocol.packet.handshake.serverbound;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;
import org.geysermc.mcprotocollib.protocol.data.handshake.HandshakeIntent;

@Data
@With
@AllArgsConstructor
public class ClientIntentionPacket implements MinecraftPacket {
    private final int protocolVersion;
    private final @NonNull String hostname;
    private final int port;
    private final @NonNull HandshakeIntent intent;

    public ClientIntentionPacket(ByteBuf in) {
        this.protocolVersion = MinecraftTypes.readVarInt(in);
        this.hostname = MinecraftTypes.readString(in);
        this.port = in.readUnsignedShort();
        this.intent = HandshakeIntent.from(MinecraftTypes.readVarInt(in) - 1);
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeVarInt(out, this.protocolVersion);
        MinecraftTypes.writeString(out, this.hostname);
        out.writeShort(this.port);
        MinecraftTypes.writeVarInt(out, this.intent.ordinal() + 1);
    }

    @Override
    public boolean isTerminal() {
        return true;
    }
}
