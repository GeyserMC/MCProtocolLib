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

@Data
@With
@AllArgsConstructor
public class ClientboundGameProfilePacket implements Packet {
    private final @NonNull GameProfile profile;

    public ClientboundGameProfilePacket(NetInput in) throws IOException {
        this.profile = new GameProfile(in.readUUID(), in.readString());
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeUUID(this.profile.getId());
        out.writeString(this.profile.getName());
    }

    @Override
    public boolean isPriority() {
        return true;
    }
}
