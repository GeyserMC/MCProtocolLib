package org.spacehq.mc.protocol.packet.ingame.server.scoreboard;

import org.spacehq.mc.protocol.data.game.values.MagicValues;
import org.spacehq.mc.protocol.data.game.values.scoreboard.ScoreboardPosition;
import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.io.NetOutput;
import org.spacehq.packetlib.packet.Packet;

import java.io.IOException;

public class ServerDisplayScoreboardPacket implements Packet {

    private ScoreboardPosition position;
    private String name;

    @SuppressWarnings("unused")
    private ServerDisplayScoreboardPacket() {
    }

    public ServerDisplayScoreboardPacket(ScoreboardPosition position, String name) {
        this.position = position;
        this.name = name;
    }

    public ScoreboardPosition getPosition() {
        return this.position;
    }

    public String getScoreboardName() {
        return this.name;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.position = MagicValues.key(ScoreboardPosition.class, in.readByte());
        this.name = in.readString();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeByte(MagicValues.value(Integer.class, this.position));
        out.writeString(this.name);
    }

    @Override
    public boolean isPriority() {
        return false;
    }

}
