package org.geysermc.mcprotocollib.protocol.packet.login.clientbound;

import com.github.steveice10.mc.auth.data.GameProfile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

import java.util.ArrayList;
import java.util.List;

@Data
@With
@AllArgsConstructor
public class ClientboundGameProfilePacket implements MinecraftPacket {
    private final @NonNull GameProfile profile;
    private final boolean strictErrorHandling;

    public ClientboundGameProfilePacket(MinecraftByteBuf buf) {
        GameProfile profile = new GameProfile(buf.readUUID(), buf.readString());
        int properties = buf.readVarInt();
        List<GameProfile.Property> propertyList = new ArrayList<>();
        for (int index = 0; index < properties; index++) {
            String propertyName = buf.readString();
            String value = buf.readString();
            String signature = null;
            if (buf.readBoolean()) {
                signature = buf.readString();
            }

            propertyList.add(new GameProfile.Property(propertyName, value, signature));
        }

        profile.setProperties(propertyList);
        this.profile = profile;
        this.strictErrorHandling = buf.readBoolean();
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeUUID(this.profile.getId());
        buf.writeString(this.profile.getName());
        buf.writeVarInt(this.profile.getProperties().size());
        for (GameProfile.Property property : this.profile.getProperties()) {
            buf.writeString(property.getName());
            buf.writeString(property.getValue());
            buf.writeBoolean(property.hasSignature());
            if (property.hasSignature()) {
                buf.writeString(property.getSignature());
            }
        }
        buf.writeBoolean(this.strictErrorHandling);
    }

    @Override
    public boolean isPriority() {
        return true;
    }
}
