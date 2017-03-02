package com.github.steveice10.mc.protocol.packet.ingame.server.entity;

import com.github.steveice10.mc.protocol.data.game.entity.player.Animation;
import com.github.steveice10.mc.protocol.util.ReflectionToString;
import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;

import java.io.IOException;

public class ServerEntityAnimationPacket implements Packet {

    private int entityId;
    private Animation animation;

    @SuppressWarnings("unused")
    private ServerEntityAnimationPacket() {
    }

    public ServerEntityAnimationPacket(int entityId, Animation animation) {
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
        this.animation = MagicValues.key(Animation.class, in.readUnsignedByte());
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

    @Override
    public String toString() {
        return ReflectionToString.toString(this);
    }
}
