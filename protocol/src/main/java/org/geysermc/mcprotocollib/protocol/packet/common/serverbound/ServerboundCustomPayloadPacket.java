package org.geysermc.mcprotocollib.protocol.packet.common.serverbound;

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
public class ServerboundCustomPayloadPacket implements MinecraftPacket {
    private static final Key BRAND = Key.key("minecraft:brand");
    private static final int MAX_UNKNOWN_PAYLOAD_SIZE = 32767;
    private final Key channel;
    private final byte [] data;

    public ServerboundCustomPayloadPacket(ByteBuf in) {
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
}
