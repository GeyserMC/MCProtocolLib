package org.spacehq.mc.protocol.packet.ingame.server.entity.spawn;

import org.spacehq.mc.protocol.data.game.EntityMetadata;
import org.spacehq.mc.protocol.data.game.values.MagicValues;
import org.spacehq.mc.protocol.data.game.values.entity.MobType;
import org.spacehq.mc.protocol.util.NetUtil;
import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.io.NetOutput;
import org.spacehq.packetlib.packet.Packet;

import java.io.IOException;

public class ServerSpawnMobPacket implements Packet {

    private int entityId;
    private MobType type;
    private double x;
    private double y;
    private double z;
    private float pitch;
    private float yaw;
    private float headYaw;
    private double motX;
    private double motY;
    private double motZ;
    private EntityMetadata metadata[];

    @SuppressWarnings("unused")
    private ServerSpawnMobPacket() {
    }

    public ServerSpawnMobPacket(int entityId, MobType type, double x, double y, double z, float yaw, float pitch, float headYaw, double motX, double motY, double motZ, EntityMetadata metadata[]) {
        this.entityId = entityId;
        this.type = type;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.headYaw = headYaw;
        this.motX = motX;
        this.motY = motY;
        this.motZ = motZ;
        this.metadata = metadata;
    }

    public int getEntityId() {
        return this.entityId;
    }

    public MobType getType() {
        return this.type;
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

    public float getHeadYaw() {
        return this.headYaw;
    }

    public double getMotionX() {
        return this.motX;
    }

    public double getMotionY() {
        return this.motY;
    }

    public double getMotionZ() {
        return this.motZ;
    }

    public EntityMetadata[] getMetadata() {
        return this.metadata;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.entityId = in.readVarInt();
        this.type = MagicValues.key(MobType.class, in.readByte());
        this.x = in.readInt() / 32D;
        this.y = in.readInt() / 32D;
        this.z = in.readInt() / 32D;
        this.yaw = in.readByte() * 360 / 256f;
        this.pitch = in.readByte() * 360 / 256f;
        this.headYaw = in.readByte() * 360 / 256f;
        this.motX = in.readShort() / 8000D;
        this.motY = in.readShort() / 8000D;
        this.motZ = in.readShort() / 8000D;
        this.metadata = NetUtil.readEntityMetadata(in);
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(this.entityId);
        out.writeByte(MagicValues.value(Integer.class, this.type));
        out.writeInt((int) (this.x * 32));
        out.writeInt((int) (this.y * 32));
        out.writeInt((int) (this.z * 32));
        out.writeByte((byte) (this.yaw * 256 / 360));
        out.writeByte((byte) (this.pitch * 256 / 360));
        out.writeByte((byte) (this.headYaw * 256 / 360));
        out.writeShort((int) (this.motX * 8000));
        out.writeShort((int) (this.motY * 8000));
        out.writeShort((int) (this.motZ * 8000));
        NetUtil.writeEntityMetadata(out, this.metadata);
    }

    @Override
    public boolean isPriority() {
        return false;
    }

}
