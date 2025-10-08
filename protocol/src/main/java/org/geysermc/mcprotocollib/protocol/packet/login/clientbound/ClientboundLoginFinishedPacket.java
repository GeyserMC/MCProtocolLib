package org.geysermc.mcprotocollib.protocol.packet.login.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.geysermc.mcprotocollib.auth.GameProfile;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;

@Data
@With
@AllArgsConstructor
public class ClientboundLoginFinishedPacket implements MinecraftPacket {
    private final @NonNull GameProfile profile;

    public ClientboundLoginFinishedPacket(ByteBuf in) {
        this.profile = MinecraftTypes.readStaticGameProfile(in);
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeStaticGameProfile(out, profile);
    }

    @Override
    public boolean isTerminal() {
        return true;
    }
}
