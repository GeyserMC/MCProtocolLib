package org.geysermc.mcprotocollib.protocol.packet.common.clientbound;

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
public class ClientboundCustomPayloadPacket implements MinecraftPacket {
    private final @NonNull Key channel;
    private final byte @NonNull [] data;

    public ClientboundCustomPayloadPacket(ByteBuf in) {
        this.channel = MinecraftTypes.readResourceLocation(in);
        this.data = MinecraftTypes.readByteArray(in, ByteBuf::readableBytes);
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeResourceLocation(out, this.channel);
        out.writeBytes(this.data);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        // GAME THREAD DETAIL: Only non-discarded payloads are handled async.
        return false; // False, you need to handle making it async yourself
    }
}
