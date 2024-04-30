package org.geysermc.mcprotocollib.protocol.packet.login.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ClientboundCustomQueryPacket implements MinecraftPacket {
    private final int messageId;
    private final @NonNull String channel;
    private final byte @NonNull [] data;

    public ClientboundCustomQueryPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.messageId = helper.readVarInt(in);
        this.channel = helper.readString(in);
        this.data = helper.readByteArray(in, ByteBuf::readableBytes);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writeVarInt(out, this.messageId);
        helper.writeString(out, this.channel);
        out.writeBytes(this.data);
    }
}
