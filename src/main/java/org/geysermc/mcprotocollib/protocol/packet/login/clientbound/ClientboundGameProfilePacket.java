package org.geysermc.mcprotocollib.protocol.packet.login.clientbound;

import com.github.steveice10.mc.auth.data.GameProfile;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;

import java.util.ArrayList;
import java.util.List;

@Data
@With
@AllArgsConstructor
public class ClientboundGameProfilePacket implements MinecraftPacket {
    private final @NonNull GameProfile profile;

    public ClientboundGameProfilePacket(ByteBuf in, MinecraftCodecHelper helper) {
        GameProfile profile = new GameProfile(helper.readUUID(in), helper.readString(in));
        int properties = helper.readVarInt(in);
        List<GameProfile.Property> propertyList = new ArrayList<>();
        for (int index = 0; index < properties; index++) {
            String propertyName = helper.readString(in);
            String value = helper.readString(in);
            String signature = null;
            if (in.readBoolean()) {
                signature = helper.readString(in);
            }

            propertyList.add(new GameProfile.Property(propertyName, value, signature));
        }

        profile.setProperties(propertyList);
        this.profile = profile;
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writeUUID(out, this.profile.getId());
        helper.writeString(out, this.profile.getName());
        helper.writeVarInt(out, this.profile.getProperties().size());
        for (GameProfile.Property property : this.profile.getProperties()) {
            helper.writeString(out, property.getName());
            helper.writeString(out, property.getValue());
            out.writeBoolean(property.hasSignature());
            if (property.hasSignature()) {
                helper.writeString(out, property.getSignature());
            }
        }
    }

    @Override
    public boolean isPriority() {
        return true;
    }
}
