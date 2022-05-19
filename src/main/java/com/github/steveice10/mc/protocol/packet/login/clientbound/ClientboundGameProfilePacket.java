package com.github.steveice10.mc.protocol.packet.login.clientbound;

import com.github.steveice10.mc.auth.data.GameProfile;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Data
@With
@AllArgsConstructor
public class ClientboundGameProfilePacket implements Packet {
    private final @NonNull GameProfile profile;

    public ClientboundGameProfilePacket(NetInput in) throws IOException {
        GameProfile profile = new GameProfile(in.readUUID(), in.readString());
        int properties = in.readVarInt();
        List<GameProfile.Property> propertyList = new ArrayList<>();
        for (int index = 0; index < properties; index++) {
            String propertyName = in.readString();
            String value = in.readString();
            String signature = null;
            if (in.readBoolean()) {
                signature = in.readString();
            }

            propertyList.add(new GameProfile.Property(propertyName, value, signature));
        }

        profile.setProperties(propertyList);
        this.profile = profile;
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeUUID(this.profile.getId());
        out.writeString(this.profile.getName());
        out.writeVarInt(this.profile.getProperties().size());
        for (GameProfile.Property property : this.profile.getProperties()) {
            out.writeString(property.getName());
            out.writeString(property.getValue());
            out.writeBoolean(property.hasSignature());
            if (property.hasSignature()) {
                out.writeString(property.getSignature());
            }
        }
    }

    @Override
    public boolean isPriority() {
        return true;
    }
}
