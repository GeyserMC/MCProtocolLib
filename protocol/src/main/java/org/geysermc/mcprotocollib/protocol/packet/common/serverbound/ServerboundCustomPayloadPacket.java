package org.geysermc.mcprotocollib.protocol.packet.common.serverbound;

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
public class ServerboundCustomPayloadPacket implements MinecraftPacket {
    private final @NonNull Key channel;
    private final byte @NonNull [] data;

    public ServerboundCustomPayloadPacket(ByteBuf in) {
        this.channel = MinecraftTypes.readResourceLocation(in);
        this.data = MinecraftTypes.readByteArray(in, ByteBuf::readableBytes);
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeResourceLocation(out, this.channel);
        out.writeBytes(this.data);
    }
}
