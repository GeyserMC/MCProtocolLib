package com.github.steveice10.mc.protocol.packet.ingame.server.entity;

import com.github.steveice10.mc.protocol.util.ReflectionToString;

public class ServerEntityRotationPacket extends ServerEntityMovementPacket {

    protected ServerEntityRotationPacket() {
        this.rot = true;
    }

    public ServerEntityRotationPacket(int entityId, float yaw, float pitch, boolean onGround) {
        super(entityId, onGround);
        this.rot = true;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    @Override
    public String toString() {
        return ReflectionToString.toString(this);
    }
}
