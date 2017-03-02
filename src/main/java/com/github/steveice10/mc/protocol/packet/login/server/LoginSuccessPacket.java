package com.github.steveice10.mc.protocol.packet.login.server;

import com.github.steveice10.mc.protocol.util.ReflectionToString;
import com.github.steveice10.mc.auth.data.GameProfile;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;

import java.io.IOException;

public class LoginSuccessPacket implements Packet {

    private GameProfile profile;

    @SuppressWarnings("unused")
    private LoginSuccessPacket() {
    }

    public LoginSuccessPacket(GameProfile profile) {
        this.profile = profile;
    }

    public GameProfile getProfile() {
        return this.profile;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.profile = new GameProfile(in.readString(), in.readString());
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeString(this.profile.getIdAsString());
        out.writeString(this.profile.getName());
    }

    @Override
    public boolean isPriority() {
        return true;
    }

    @Override
    public String toString() {
        return ReflectionToString.toString(this);
    }
}
