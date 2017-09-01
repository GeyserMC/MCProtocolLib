package com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn;

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.entity.type.object.FallingBlockData;
import com.github.steveice10.mc.protocol.data.game.entity.type.object.HangingDirection;
import com.github.steveice10.mc.protocol.data.game.entity.type.object.MinecartType;
import com.github.steveice10.mc.protocol.data.game.entity.type.object.ObjectData;
import com.github.steveice10.mc.protocol.data.game.entity.type.object.ObjectType;
import com.github.steveice10.mc.protocol.data.game.entity.type.object.ProjectileData;
import com.github.steveice10.mc.protocol.data.game.entity.type.object.SplashPotionData;
import com.github.steveice10.mc.protocol.packet.MinecraftPacket;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;

import java.io.IOException;
import java.util.UUID;

public class ServerSpawnObjectPacket extends MinecraftPacket {
    private int entityId;
    private UUID uuid;
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

    public ServerSpawnObjectPacket(int entityId, UUID uuid, ObjectType type, double x, double y, double z, float yaw, float pitch) {
        this(entityId, uuid, type, null, x, y, z, yaw, pitch, 0, 0, 0);
    }

    public ServerSpawnObjectPacket(int entityId, UUID uuid, ObjectType type, ObjectData data, double x, double y, double z, float yaw, float pitch) {
        this(entityId, uuid, type, data, x, y, z, yaw, pitch, 0, 0, 0);
    }

    public ServerSpawnObjectPacket(int entityId, UUID uuid, ObjectType type, double x, double y, double z, float yaw, float pitch, double motX, double motY, double motZ) {
        this(entityId, uuid, type, new ObjectData() {
        }, x, y, z, yaw, pitch, motX, motY, motZ);
    }

    public ServerSpawnObjectPacket(int entityId, UUID uuid, ObjectType type, ObjectData data, double x, double y, double z, float yaw, float pitch, double motX, double motY, double motZ) {
        this.entityId = entityId;
        this.uuid = uuid;
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

    public UUID getUUID() {
        return this.uuid;
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
        this.uuid = in.readUUID();
        this.type = MagicValues.key(ObjectType.class, in.readByte());
        this.x = in.readDouble();
        this.y = in.readDouble();
        this.z = in.readDouble();
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
            } else if(this.type == ObjectType.SPECTRAL_ARROW || this.type == ObjectType.TIPPED_ARROW || this.type == ObjectType.GHAST_FIREBALL || this.type == ObjectType.BLAZE_FIREBALL || this.type == ObjectType.DRAGON_FIREBALL || this.type == ObjectType.WITHER_HEAD_PROJECTILE || this.type == ObjectType.FISH_HOOK) {
                this.data = new ProjectileData(data);
            } else {
                this.data = new ObjectData() {
                };
            }
        }

        this.motX = in.readShort() / 8000D;
        this.motY = in.readShort() / 8000D;
        this.motZ = in.readShort() / 8000D;
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(this.entityId);
        out.writeUUID(this.uuid);
        out.writeByte(MagicValues.value(Integer.class, this.type));
        out.writeDouble(this.x);
        out.writeDouble(this.y);
        out.writeDouble(this.z);
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

        out.writeShort((int) (this.motX * 8000));
        out.writeShort((int) (this.motY * 8000));
        out.writeShort((int) (this.motZ * 8000));
    }
}
