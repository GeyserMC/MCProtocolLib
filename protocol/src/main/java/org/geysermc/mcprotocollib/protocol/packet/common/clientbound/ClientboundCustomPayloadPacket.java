package org.geysermc.mcprotocollib.protocol.packet.common.clientbound;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ClientboundCustomPayloadPacket implements MinecraftPacket {
    private final @NonNull String channel;
    private final byte @NonNull [] data;

    public ClientboundCustomPayloadPacket(MinecraftByteBuf buf) {
        this.channel = buf.readString();
        this.data = buf.readByteArray(buf::readableBytes);
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeString(this.channel);
        buf.writeBytes(this.data);
    }
}
