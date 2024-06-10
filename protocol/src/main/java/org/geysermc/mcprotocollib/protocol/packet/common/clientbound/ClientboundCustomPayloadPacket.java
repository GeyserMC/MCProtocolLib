package org.geysermc.mcprotocollib.protocol.packet.common.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import net.kyori.adventure.key.Key;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ClientboundCustomPayloadPacket implements MinecraftPacket {
    private final @NonNull Key channel;
    private final byte @NonNull [] data;

    public ClientboundCustomPayloadPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.channel = helper.readResourceLocation(in);
        this.data = helper.readByteArray(in, ByteBuf::readableBytes);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writeResourceLocation(out, this.channel);
        out.writeBytes(this.data);
    }
}
