package org.geysermc.mcprotocollib.protocol.packet.login.clientbound;

import com.github.steveice10.mc.auth.data.GameProfile;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ClientboundGameProfilePacket implements MinecraftPacket {
    private final @NonNull GameProfile profile;
    private final boolean strictErrorHandling;

    public ClientboundGameProfilePacket(ByteBuf in, MinecraftCodecHelper helper) {
        GameProfile profile = new GameProfile(helper.readUUID(in), helper.readString(in));
        profile.setProperties(helper.readList(in, helper::readProperty));
        this.profile = profile;
        this.strictErrorHandling = in.readBoolean();
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writeUUID(out, this.profile.getId());
        helper.writeString(out, this.profile.getName());
        helper.writeList(out, this.profile.getProperties(), helper::writeProperty);
        out.writeBoolean(this.strictErrorHandling);
    }

    @Override
    public boolean isTerminal() {
        return true;
    }
}
