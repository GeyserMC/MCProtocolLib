package org.geysermc.mcprotocollib.protocol.packet.login.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import net.kyori.adventure.key.Key;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;

@Data
@With
@AllArgsConstructor
public class ClientboundCustomQueryPacket implements MinecraftPacket {
    private final int messageId;
    private final @NonNull Key channel;
    private final byte @NonNull [] data;

    public ClientboundCustomQueryPacket(ByteBuf in) {
        this.messageId = MinecraftTypes.readVarInt(in);
        this.channel = MinecraftTypes.readResourceLocation(in);
        this.data = MinecraftTypes.readByteArray(in, ByteBuf::readableBytes);
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeVarInt(out, this.messageId);
        MinecraftTypes.writeResourceLocation(out, this.channel);
        out.writeBytes(this.data);
    }
}
