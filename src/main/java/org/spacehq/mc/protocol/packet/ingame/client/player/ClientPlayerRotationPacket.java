package org.spacehq.mc.protocol.packet.ingame.client.player;

public class ClientPlayerRotationPacket extends ClientPlayerMovementPacket {

    protected ClientPlayerRotationPacket() {
        this.rot = true;
    }

    public ClientPlayerRotationPacket(boolean onGround, float yaw, float pitch) {
        super(onGround);
        this.rot = true;
        this.yaw = yaw;
        this.pitch = pitch;
    }

}
