package com.github.steveice10.mc.protocol.packet.handshake.serverbound;

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.handshake.HandshakeIntent;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;

import java.io.IOException;

@Data
@With
@AllArgsConstructor
public class ClientIntentionPacket implements Packet {
    private final int protocolVersion;
    private final @NonNull String hostname;
    private final int port;
    private final @NonNull HandshakeIntent intent;

    public ClientIntentionPacket(NetInput in) throws IOException {
        this.protocolVersion = in.readVarInt();
        this.hostname = in.readString();
        this.port = in.readUnsignedShort();
        this.intent = MagicValues.key(HandshakeIntent.class, in.readVarInt());
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(this.protocolVersion);
        out.writeString(this.hostname);
        out.writeShort(this.port);
        out.writeVarInt(MagicValues.value(Integer.class, this.intent));
    }

    @Override
    public boolean isPriority() {
        return true;
    }
}
