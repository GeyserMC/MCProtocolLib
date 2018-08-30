package com.github.steveice10.mc.protocol.packet.ingame.client.window;

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
import com.github.steveice10.mc.protocol.data.game.world.block.CommandBlockMode;
import com.github.steveice10.mc.protocol.packet.MinecraftPacket;
import com.github.steveice10.mc.protocol.util.NetUtil;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;

import java.io.IOException;

public class ClientUpdateCommandBlockPacket extends MinecraftPacket {
    private Position position;
    private String command;
    private CommandBlockMode mode;
    private boolean doesTrackOutput;
    private boolean isConditional;
    private boolean isAutomatic;

    @SuppressWarnings("unused")
    private ClientUpdateCommandBlockPacket() {
    }

    public ClientUpdateCommandBlockPacket(Position position, String command, CommandBlockMode mode,
                                          boolean doesTrackOutput, boolean isConditional, boolean isAutomatic) {
        this.position = position;
        this.command = command;
        this.mode = mode;
        this.doesTrackOutput = doesTrackOutput;
        this.isConditional = isConditional;
        this.isAutomatic = isAutomatic;
    }

    public Position getPosition() {
        return this.position;
    }

    public String getCommand() {
        return this.command;
    }

    public CommandBlockMode getMode() {
        return this.mode;
    }

    public boolean isDoesTrackOutput() {
        return this.doesTrackOutput;
    }

    public boolean isConditional() {
        return this.isConditional;
    }

    public boolean isAutomatic() {
        return this.isAutomatic;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.position = NetUtil.readPosition(in);
        this.command = in.readString();
        this.mode = MagicValues.key(CommandBlockMode.class, in.readVarInt());
        int flags = in.readUnsignedByte();
        this.doesTrackOutput = (flags & 0x01) != 0;
        this.isConditional = (flags & 0x02) != 0;
        this.isAutomatic = (flags & 0x04) != 0;
    }

    @Override
    public void write(NetOutput out) throws IOException {
        NetUtil.writePosition(out, this.position);
        out.writeString(this.command);
        out.writeVarInt(MagicValues.value(Integer.class, this.mode));
        int flags = 0;
        if (this.doesTrackOutput) flags |= 0x01;
        if (this.isConditional) flags |= 0x02;
        if (this.isAutomatic) flags |= 0x04;
        out.writeByte(flags);
    }
}
