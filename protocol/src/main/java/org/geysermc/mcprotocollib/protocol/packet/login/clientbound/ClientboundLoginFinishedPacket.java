package org.geysermc.mcprotocollib.protocol.packet.login.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.geysermc.mcprotocollib.auth.GameProfile;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;

import java.util.UUID;

@Data
@With
@AllArgsConstructor
public class ClientboundLoginFinishedPacket implements MinecraftPacket {
    private final @NonNull GameProfile profile;
    private final @NonNull UUID sessionId;

    public ClientboundLoginFinishedPacket(ByteBuf in) {
        this.profile = MinecraftTypes.readStaticGameProfile(in);
        this.sessionId = MinecraftTypes.readUUID(in);
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeStaticGameProfile(out, this.profile);
        MinecraftTypes.writeUUID(out, this.sessionId);
    }

    @Override
    public boolean isTerminal() {
        return true;
    }
}
