package org.spacehq.mc.protocol.packet.ingame.server.entity;

import org.spacehq.mc.protocol.data.game.values.MagicValues;
import org.spacehq.mc.protocol.data.game.values.entity.player.Animation;
import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.io.NetOutput;
import org.spacehq.packetlib.packet.Packet;

import java.io.IOException;

public class ServerAnimationPacket implements Packet {

    private int entityId;
    private Animation animation;

    @SuppressWarnings("unused")
    private ServerAnimationPacket() {
    }

    public ServerAnimationPacket(int entityId, Animation animation) {
        this.entityId = entityId;
        this.animation = animation;
    }

    public int getEntityId() {
        return this.entityId;
    }

    public Animation getAnimation() {
        return this.animation;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.entityId = in.readVarInt();
        this.animation = MagicValues.key(Animation.class, in.readByte());
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(this.entityId);
        out.writeByte(MagicValues.value(Integer.class, this.animation));
    }

    @Override
    public boolean isPriority() {
        return false;
    }

}
