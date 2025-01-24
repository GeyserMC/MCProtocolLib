package org.geysermc.mcprotocollib.protocol.packet.login.serverbound;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;

import java.util.UUID;

@Data
@With
@AllArgsConstructor
public class ServerboundHelloPacket implements MinecraftPacket {
    private final @NonNull String username;
    private final @NonNull UUID profileId;

    public ServerboundHelloPacket(ByteBuf in) {
        this.username = MinecraftTypes.readString(in);
        this.profileId = MinecraftTypes.readUUID(in);
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeString(out, this.username);
        MinecraftTypes.writeUUID(out, this.profileId);
    }
}
