package org.spacehq.mc.protocol.packet.ingame.server.entity.spawn;

import org.spacehq.mc.protocol.data.game.values.MagicValues;
import org.spacehq.mc.protocol.data.game.values.entity.FallingBlockData;
import org.spacehq.mc.protocol.data.game.values.entity.HangingDirection;
import org.spacehq.mc.protocol.data.game.values.entity.MinecartType;
import org.spacehq.mc.protocol.data.game.values.entity.ObjectData;
import org.spacehq.mc.protocol.data.game.values.entity.ObjectType;
import org.spacehq.mc.protocol.data.game.values.entity.ProjectileData;
import org.spacehq.mc.protocol.data.game.values.entity.SplashPotionData;
import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.io.NetOutput;
import org.spacehq.packetlib.packet.Packet;

import java.io.IOException;

public class ServerSpawnObjectPacket implements Packet {

    private int entityId;
    private ObjectType type;
    private double x;
    private double y;
    private double z;
    private float pitch;
    private float yaw;
    private ObjectData data;
    private double motX;
    private double motY;
    private double motZ;

    @SuppressWarnings("unused")
    private ServerSpawnObjectPacket() {
    }

    public ServerSpawnObjectPacket(int entityId, ObjectType type, double x, double y, double z, float yaw, float pitch) {
        this(entityId, type, null, x, y, z, yaw, pitch, 0, 0, 0);
    }

    public ServerSpawnObjectPacket(int entityId, ObjectType type, ObjectData data, double x, double y, double z, float yaw, float pitch) {
        this(entityId, type, data, x, y, z, yaw, pitch, 0, 0, 0);
    }

    public ServerSpawnObjectPacket(int entityId, ObjectType type, double x, double y, double z, float yaw, float pitch, double motX, double motY, double motZ) {
        this(entityId, type, new ObjectData() {
        }, x, y, z, yaw, pitch, motX, motY, motZ);
    }

    public ServerSpawnObjectPacket(int entityId, ObjectType type, ObjectData data, double x, double y, double z, float yaw, float pitch, double motX, double motY, double motZ) {
        this.entityId = entityId;
        this.type = type;
        this.data = data;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.motX = motX;
        this.motY = motY;
        this.motZ = motZ;
    }

    public int getEntityId() {
        return this.entityId;
    }

    public ObjectType getType() {
        return this.type;
    }

    public ObjectData getData() {
        return this.data;
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

    public double getMotionX() {
        return this.motX;
    }

    public double getMotionY() {
        return this.motY;
    }

    public double getMotionZ() {
        return this.motZ;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.entityId = in.readVarInt();
        this.type = MagicValues.key(ObjectType.class, in.readByte());
        this.x = in.readInt() / 32D;
        this.y = in.readInt() / 32D;
        this.z = in.readInt() / 32D;
        this.pitch = in.readByte() * 360 / 256f;
        this.yaw = in.readByte() * 360 / 256f;
        int data = in.readInt();
        if(data > 0) {
            if(this.type == ObjectType.MINECART) {
                this.data = MagicValues.key(MinecartType.class, data);
            } else if(this.type == ObjectType.ITEM_FRAME) {
                this.data = MagicValues.key(HangingDirection.class, data);
            } else if(this.type == ObjectType.FALLING_BLOCK) {
                this.data = new FallingBlockData(data & 65535, data >> 16);
            } else if(this.type == ObjectType.POTION) {
                this.data = new SplashPotionData(data);
            } else if(this.type == ObjectType.ARROW || this.type == ObjectType.BLAZE_FIREBALL || this.type == ObjectType.FISH_HOOK || this.type == ObjectType.GHAST_FIREBALL || this.type == ObjectType.WITHER_HEAD_PROJECTILE) {
                this.data = new ProjectileData(data);
            } else {
                this.data = new ObjectData() {
                };
            }

            this.motX = in.readShort() / 8000D;
            this.motY = in.readShort() / 8000D;
            this.motZ = in.readShort() / 8000D;
        }
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(this.entityId);
        out.writeByte(MagicValues.value(Integer.class, this.type));
        out.writeInt((int) (this.x * 32));
        out.writeInt((int) (this.y * 32));
        out.writeInt((int) (this.z * 32));
        out.writeByte((byte) (this.pitch * 256 / 360));
        out.writeByte((byte) (this.yaw * 256 / 360));
        int data = 0;
        if(this.data != null) {
            if(this.data instanceof MinecartType) {
                data = MagicValues.value(Integer.class, (Enum<?>) this.data);
            } else if(this.data instanceof HangingDirection) {
                data = MagicValues.value(Integer.class, (Enum<?>) this.data);
            } else if(this.data instanceof FallingBlockData) {
                data = ((FallingBlockData) this.data).getId() | ((FallingBlockData) this.data).getMetadata() << 16;
            } else if(this.data instanceof SplashPotionData) {
                data = ((SplashPotionData) this.data).getPotionData();
            } else if(this.data instanceof ProjectileData) {
                data = ((ProjectileData) this.data).getOwnerId();
            } else {
                data = 1;
            }
        }

        out.writeInt(data);
        if(data > 0) {
            out.writeShort((int) (this.motX * 8000));
            out.writeShort((int) (this.motY * 8000));
            out.writeShort((int) (this.motZ * 8000));
        }
    }

    @Override
    public boolean isPriority() {
        return false;
    }

}
