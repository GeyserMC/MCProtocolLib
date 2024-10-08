package org.geysermc.mcprotocollib.protocol.packet.handshake.serverbound;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
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

    public ClientIntentionPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.protocolVersion = helper.readVarInt(in);
        this.hostname = helper.readString(in);
        this.port = in.readUnsignedShort();
        this.intent = HandshakeIntent.from(helper.readVarInt(in) - 1);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writeVarInt(out, this.protocolVersion);
        helper.writeString(out, this.hostname);
        out.writeShort(this.port);
        helper.writeVarInt(out, this.intent.ordinal() + 1);
    }

    @Override
    public boolean isPriority() {
        return true;
    }

    @Override
    public boolean isTerminal() {
        return true;
    }
}
