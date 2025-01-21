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
        GameProfile profile = new GameProfile(MinecraftTypes.readUUID(in), MinecraftTypes.readString(in));
        profile.setProperties(MinecraftTypes.readList(in, MinecraftTypes::readProperty));
        this.profile = profile;
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeUUID(out, this.profile.getId());
        MinecraftTypes.writeString(out, this.profile.getName());
        MinecraftTypes.writeList(out, this.profile.getProperties(), MinecraftTypes::writeProperty);
    }

    @Override
    public boolean isTerminal() {
        return true;
    }
}
