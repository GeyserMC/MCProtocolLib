package org.spacehq.mc.protocol.packet.ingame.client.player;

import org.spacehq.mc.protocol.data.game.values.MagicValues;
import org.spacehq.mc.protocol.data.game.values.entity.player.InteractAction;
import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.io.NetOutput;
import org.spacehq.packetlib.packet.Packet;

import java.io.IOException;

public class ClientPlayerInteractEntityPacket implements Packet {

    private int entityId;
    private InteractAction action;

    private float targetX;
    private float targetY;
    private float targetZ;

    @SuppressWarnings("unused")
    private ClientPlayerInteractEntityPacket() {
    }

    public ClientPlayerInteractEntityPacket(int entityId, InteractAction action) {
        this(entityId, action, 0, 0, 0);
    }

    public ClientPlayerInteractEntityPacket(int entityId, InteractAction action, float targetX, float targetY, float targetZ) {
        this.entityId = entityId;
        this.action = action;
        this.targetX = targetX;
        this.targetY = targetY;
        this.targetZ = targetZ;
    }

    public int getEntityId() {
        return this.entityId;
    }

    public InteractAction getAction() {
        return this.action;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.entityId = in.readVarInt();
        this.action = MagicValues.key(InteractAction.class, in.readVarInt());
        if(this.action == InteractAction.INTERACT_AT) {
            this.targetX = in.readFloat();
            this.targetY = in.readFloat();
            this.targetZ = in.readFloat();
        }
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(this.entityId);
        out.writeVarInt(MagicValues.value(Integer.class, this.action));
        if(this.action == InteractAction.INTERACT_AT) {
            out.writeFloat(this.targetX);
            out.writeFloat(this.targetY);
            out.writeFloat(this.targetZ);
        }
    }

    @Override
    public boolean isPriority() {
        return false;
    }

}
