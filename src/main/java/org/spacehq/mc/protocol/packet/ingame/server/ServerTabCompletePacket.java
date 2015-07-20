package org.spacehq.mc.protocol.packet.ingame.server;

import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.io.NetOutput;
import org.spacehq.packetlib.packet.Packet;

import java.io.IOException;

public class ServerTabCompletePacket implements Packet {

    private String matches[];

    @SuppressWarnings("unused")
    private ServerTabCompletePacket() {
    }

    public ServerTabCompletePacket(String matches[]) {
        this.matches = matches;
    }

    public String[] getMatches() {
        return this.matches;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.matches = new String[in.readVarInt()];
        for(int index = 0; index < this.matches.length; index++) {
            this.matches[index] = in.readString();
        }
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(this.matches.length);
        for(String match : this.matches) {
            out.writeString(match);
        }
    }

    @Override
    public boolean isPriority() {
        return false;
    }

}
