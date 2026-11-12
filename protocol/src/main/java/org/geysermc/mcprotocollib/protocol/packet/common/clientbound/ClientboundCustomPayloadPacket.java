package org.geysermc.mcprotocollib.protocol.packet.common.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import net.kyori.adventure.key.Key;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;

@Data
@With
@AllArgsConstructor
public class ClientboundCustomPayloadPacket implements MinecraftPacket {
    private static final Key BRAND = Key.key("minecraft:brand");
    private static final int MAX_UNKNOWN_PAYLOAD_SIZE = 1048576;
    private final Key channel;
    private final byte [] data;

    public ClientboundCustomPayloadPacket(ByteBuf in) {
        this.channel = MinecraftTypes.readResourceLocation(in);
        this.validateSize(in.readableBytes());
        this.data = MinecraftTypes.readByteArray(in, ByteBuf::readableBytes);
    }

    @Override
    public void serialize(ByteBuf out) {
        this.validateSize(this.data.length);
        MinecraftTypes.writeResourceLocation(out, this.channel);
        out.writeBytes(this.data);
    }

    private void validateSize(int length) {
        if (!this.channel.equals(BRAND) && length > MAX_UNKNOWN_PAYLOAD_SIZE) {
            throw new IllegalArgumentException("Unknown custom payload exceeds maximum size: " + MAX_UNKNOWN_PAYLOAD_SIZE);
        }
    }

    @Override
    public boolean shouldRunOnGameThread() {
        // GAME THREAD DETAIL: Only non-discarded payloads are handled async.
        return false; // False, you need to handle making it async yourself
    }
}
