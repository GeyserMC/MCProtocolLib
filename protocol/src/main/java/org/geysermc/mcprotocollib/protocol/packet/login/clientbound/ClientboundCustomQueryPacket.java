package org.geysermc.mcprotocollib.protocol.packet.login.clientbound;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ClientboundCustomQueryPacket implements MinecraftPacket {
    private final int messageId;
    private final @NonNull String channel;
    private final byte @NonNull [] data;

    public ClientboundCustomQueryPacket(MinecraftByteBuf buf) {
        this.messageId = buf.readVarInt();
        this.channel = buf.readString();
        this.data = buf.readByteArray(buf::readableBytes);
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeVarInt(this.messageId);
        buf.writeString(this.channel);
        buf.writeBytes(this.data);
    }
}
