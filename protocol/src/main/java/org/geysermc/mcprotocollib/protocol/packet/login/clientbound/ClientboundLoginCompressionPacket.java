package org.geysermc.mcprotocollib.protocol.packet.login.clientbound;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ClientboundLoginCompressionPacket implements MinecraftPacket {
    private final int threshold;

    public ClientboundLoginCompressionPacket(MinecraftByteBuf buf) {
        this.threshold = buf.readVarInt();
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeVarInt(this.threshold);
    }

    @Override
    public boolean isPriority() {
        return true;
    }
}
