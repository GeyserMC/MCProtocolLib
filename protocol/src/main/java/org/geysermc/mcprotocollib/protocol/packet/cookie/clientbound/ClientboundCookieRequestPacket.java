package org.geysermc.mcprotocollib.protocol.packet.cookie.clientbound;

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
public class ClientboundCookieRequestPacket implements MinecraftPacket {
    private final Key key;

    public ClientboundCookieRequestPacket(ByteBuf in) {
        this.key = MinecraftTypes.readResourceLocation(in);
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeResourceLocation(out, this.key);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
