package org.geysermc.mcprotocollib.protocol.packet.cookie.serverbound;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import net.kyori.adventure.key.Key;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;
import org.jetbrains.annotations.Nullable;

@Data
@With
@AllArgsConstructor
public class ServerboundCookieResponsePacket implements MinecraftPacket {
    private final Key key;
    private final byte @Nullable [] payload;

    public ServerboundCookieResponsePacket(ByteBuf in) {
        this.key = MinecraftTypes.readResourceLocation(in);
        this.payload = MinecraftTypes.readNullable(in, MinecraftTypes::readByteArray);
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeResourceLocation(out, this.key);
        MinecraftTypes.writeNullable(out, this.payload, MinecraftTypes::writeByteArray);
    }
}
