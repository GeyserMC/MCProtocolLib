package com.github.steveice10.mc.protocol.packet.ingame.client.window;

import com.github.steveice10.mc.protocol.packet.MinecraftPacket;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;

import java.io.IOException;

public class ClientUpdateCommandBlockMinecartPacket extends MinecraftPacket {
    private int entityId;
    private String command;
    private boolean doesTrackOutput;

    @SuppressWarnings("unused")
    private ClientUpdateCommandBlockMinecartPacket() {
    }

    public ClientUpdateCommandBlockMinecartPacket(int entityId, String command, boolean doesTrackOutput) {
        this.entityId = entityId;
        this.command = command;
        this.doesTrackOutput = doesTrackOutput;
    }

    public int getEntityId() {
        return this.entityId;
    }

    public String getCommand() {
        return this.command;
    }

    public boolean isDoesTrackOutput() {
        return this.doesTrackOutput;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.entityId = in.readVarInt();
        this.command = in.readString();
        this.doesTrackOutput = in.readBoolean();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(this.entityId);
        out.writeString(this.command);
        out.writeBoolean(this.doesTrackOutput);
    }
}
