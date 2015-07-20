package org.spacehq.mc.protocol.packet.ingame.server.entity.player;

import org.spacehq.mc.protocol.data.game.values.MagicValues;
import org.spacehq.mc.protocol.data.game.values.entity.player.PositionElement;
import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.io.NetOutput;
import org.spacehq.packetlib.packet.Packet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ServerPlayerPositionRotationPacket implements Packet {

    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;
    private List<PositionElement> relative;

    @SuppressWarnings("unused")
    private ServerPlayerPositionRotationPacket() {
    }

    public ServerPlayerPositionRotationPacket(double x, double y, double z, float yaw, float pitch) {
        this(x, y, z, yaw, pitch, new PositionElement[0]);
    }

    public ServerPlayerPositionRotationPacket(double x, double y, double z, float yaw, float pitch, PositionElement... relative) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.relative = Arrays.asList(relative);
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    public float getYaw() {
        return this.yaw;
    }

    public float getPitch() {
        return this.pitch;
    }

    public List<PositionElement> getRelativeElements() {
        return this.relative;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.x = in.readDouble();
        this.y = in.readDouble();
        this.z = in.readDouble();
        this.yaw = in.readFloat();
        this.pitch = in.readFloat();
        this.relative = new ArrayList<PositionElement>();
        int flags = in.readUnsignedByte();
        for(PositionElement element : PositionElement.values()) {
            int bit = 1 << MagicValues.value(Integer.class, element);
            if((flags & bit) == bit) {
                this.relative.add(element);
            }
        }
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeDouble(this.x);
        out.writeDouble(this.y);
        out.writeDouble(this.z);
        out.writeFloat(this.yaw);
        out.writeFloat(this.pitch);
        int flags = 0;
        for(PositionElement element : this.relative) {
            flags |= 1 << MagicValues.value(Integer.class, element);
        }

        out.writeByte(flags);
    }

    @Override
    public boolean isPriority() {
        return false;
    }

}
