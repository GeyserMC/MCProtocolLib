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
public class ClientboundStoreCookiePacket implements MinecraftPacket {
    private final Key key;
    private final byte[] payload;

    public ClientboundStoreCookiePacket(ByteBuf in) {
        this.key = MinecraftTypes.readResourceLocation(in);
        this.payload = MinecraftTypes.readByteArray(in);
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeResourceLocation(out, this.key);
        MinecraftTypes.writeByteArray(out, this.payload);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
