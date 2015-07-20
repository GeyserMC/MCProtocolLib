package org.spacehq.mc.protocol.packet.ingame.server.entity;

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

}
