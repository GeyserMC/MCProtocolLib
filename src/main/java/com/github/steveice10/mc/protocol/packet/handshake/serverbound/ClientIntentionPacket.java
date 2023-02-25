package com.github.steveice10.mc.protocol.packet.handshake.serverbound;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import com.github.steveice10.mc.protocol.data.handshake.HandshakeIntent;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;

import java.io.IOException;

@Data
@With
@AllArgsConstructor
public class ClientIntentionPacket implements MinecraftPacket {
    private final int protocolVersion;
    private final @NonNull String hostname;
    private final int port;
    private final @NonNull HandshakeIntent intent;

    public ClientIntentionPacket(ByteBuf in, MinecraftCodecHelper helper) throws IOException {
        this.protocolVersion = helper.readVarInt(in);
        this.hostname = helper.readString(in);
        this.port = in.readUnsignedShort();
        this.intent = HandshakeIntent.from(helper.readVarInt(in) - 1);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
        helper.writeVarInt(out, this.protocolVersion);
        helper.writeString(out, this.hostname);
        out.writeShort(this.port);
        helper.writeVarInt(out, this.intent.ordinal() + 1);
    }

    @Override
    public boolean isPriority() {
        return true;
    }
}
