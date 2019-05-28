package com.github.steveice10.mc.protocol.packet.ingame.client.window;

import java.io.IOException;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
import com.github.steveice10.mc.protocol.packet.MinecraftPacket;
import com.github.steveice10.mc.protocol.util.NetUtil;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;

public class ClientUpdateJigsawBlockPacket extends MinecraftPacket {

    private Position position;
    private String attachmentType;
    private String targetPool;
    private String finalState;

    public ClientUpdateJigsawBlockPacket() {
    }

    public ClientUpdateJigsawBlockPacket(Position position, String attachmentType, String targetPool, String finalState) {
        this.position = position;
        this.attachmentType = attachmentType;
        this.targetPool = targetPool;
        this.finalState = finalState;
    }

    public Position getPosition() {
        return position;
    }

    public String getAttachmentType() {
        return attachmentType;
    }

    public String getTargetPool() {
        return targetPool;
    }

    public String getFinalState() {
        return finalState;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.position = NetUtil.readPosition(in);
        this.attachmentType = in.readString();
        this.targetPool = in.readString();
        this.finalState = in.readString();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        NetUtil.writePosition(out, this.position);
        out.writeString(this.attachmentType);
        out.writeString(this.targetPool);
        out.writeString(this.finalState);
    }
}
