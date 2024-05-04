package org.geysermc.mcprotocollib.protocol.packet.login.serverbound;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

import java.util.UUID;

@Data
@With
@AllArgsConstructor
public class ServerboundHelloPacket implements MinecraftPacket {
    private final @NonNull String username;
    private final @NonNull UUID profileId;

    public ServerboundHelloPacket(MinecraftByteBuf buf) {
        this.username = buf.readString();
        this.profileId = buf.readUUID();
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeString(this.username);
        buf.writeUUID(this.profileId);
    }

    @Override
    public boolean isPriority() {
        return true;
    }
}
