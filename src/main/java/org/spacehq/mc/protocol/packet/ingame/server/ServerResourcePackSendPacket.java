package org.spacehq.mc.protocol.packet.ingame.server;

import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.io.NetOutput;
import org.spacehq.packetlib.packet.Packet;

import java.io.IOException;

public class ServerResourcePackSendPacket implements Packet {
    private String url;
    private String hash;

    @SuppressWarnings("unused")
    private ServerResourcePackSendPacket() {
    }

    public ServerResourcePackSendPacket(String url, String hash) {
        this.url = url;
        this.hash = hash;
    }

    public String getUrl() {
        return this.url;
    }

    public String getHash() {
        return this.hash;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.url = in.readString();
        this.hash = in.readString();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeString(this.url);
        out.writeString(this.hash);
    }

    @Override
    public boolean isPriority() {
        return false;
    }
}
