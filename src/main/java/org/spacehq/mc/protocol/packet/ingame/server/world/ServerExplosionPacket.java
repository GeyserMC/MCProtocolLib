package org.spacehq.mc.protocol.packet.ingame.server.world;

import org.spacehq.mc.protocol.data.game.values.world.block.ExplodedBlockRecord;
import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.io.NetOutput;
import org.spacehq.packetlib.packet.Packet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ServerExplosionPacket implements Packet {

    private float x;
    private float y;
    private float z;
    private float radius;
    private List<ExplodedBlockRecord> exploded;
    private float pushX;
    private float pushY;
    private float pushZ;

    @SuppressWarnings("unused")
    private ServerExplosionPacket() {
    }

    public ServerExplosionPacket(float x, float y, float z, float radius, List<ExplodedBlockRecord> exploded, float pushX, float pushY, float pushZ) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.radius = radius;
        this.exploded = exploded;
        this.pushX = pushX;
        this.pushY = pushY;
        this.pushZ = pushZ;
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public float getZ() {
        return this.z;
    }

    public float getRadius() {
        return this.radius;
    }

    public List<ExplodedBlockRecord> getExploded() {
        return this.exploded;
    }

    public float getPushX() {
        return this.pushX;
    }

    public float getPushY() {
        return this.pushY;
    }

    public float getPushZ() {
        return this.pushZ;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.x = in.readFloat();
        this.y = in.readFloat();
        this.z = in.readFloat();
        this.radius = in.readFloat();
        this.exploded = new ArrayList<ExplodedBlockRecord>();
        int length = in.readInt();
        for(int count = 0; count < length; count++) {
            this.exploded.add(new ExplodedBlockRecord(in.readByte(), in.readByte(), in.readByte()));
        }

        this.pushX = in.readFloat();
        this.pushY = in.readFloat();
        this.pushZ = in.readFloat();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeFloat(this.x);
        out.writeFloat(this.y);
        out.writeFloat(this.z);
        out.writeFloat(this.radius);
        out.writeInt(this.exploded.size());
        for(ExplodedBlockRecord record : this.exploded) {
            out.writeByte(record.getX());
            out.writeByte(record.getY());
            out.writeByte(record.getZ());
        }

        out.writeFloat(this.pushX);
        out.writeFloat(this.pushY);
        out.writeFloat(this.pushZ);
    }

    @Override
    public boolean isPriority() {
        return false;
    }

}
