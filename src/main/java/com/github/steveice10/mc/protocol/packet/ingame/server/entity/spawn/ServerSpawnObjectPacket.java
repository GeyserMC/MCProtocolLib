package com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn;

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.entity.type.object.FallingBlockData;
import com.github.steveice10.mc.protocol.data.game.entity.type.object.GenericObjectData;
import com.github.steveice10.mc.protocol.data.game.entity.type.object.HangingDirection;
import com.github.steveice10.mc.protocol.data.game.entity.type.object.MinecartType;
import com.github.steveice10.mc.protocol.data.game.entity.type.object.ObjectData;
import com.github.steveice10.mc.protocol.data.game.entity.type.object.ObjectType;
import com.github.steveice10.mc.protocol.data.game.entity.type.object.ProjectileData;
import com.github.steveice10.mc.protocol.data.game.entity.type.object.SplashPotionData;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import java.io.IOException;
import java.util.UUID;

@Data
@Setter(AccessLevel.NONE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ServerSpawnObjectPacket implements Packet {
    private int entityId;
    private @NonNull UUID uuid;
    private @NonNull ObjectType type;
    private @NonNull ObjectData data;
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;
    private double motionX;
    private double motionY;
    private double motionZ;

    public ServerSpawnObjectPacket(int entityId, @NonNull UUID uuid, @NonNull ObjectType type,
                                   double x, double y, double z, float yaw, float pitch) {
        this(entityId, uuid, type, new GenericObjectData(0), x, y, z, yaw, pitch, 0, 0, 0);
    }

    public ServerSpawnObjectPacket(int entityId, @NonNull UUID uuid, @NonNull ObjectType type, @NonNull ObjectData data,
                                   double x, double y, double z, float yaw, float pitch) {
        this(entityId, uuid, type, data, x, y, z, yaw, pitch, 0, 0, 0);
    }

    public ServerSpawnObjectPacket(int entityId, @NonNull UUID uuid, @NonNull ObjectType type,
                                   double x, double y, double z, float yaw, float pitch,
                                   double motionX, double motionY, double motionZ) {
        this(entityId, uuid, type, new GenericObjectData(0), x, y, z, yaw, pitch, motionX, motionY, motionZ);
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.entityId = in.readVarInt();
        this.uuid = in.readUUID();
        this.type = MagicValues.key(ObjectType.class, in.readVarInt());
        this.x = in.readDouble();
        this.y = in.readDouble();
        this.z = in.readDouble();
        this.pitch = in.readByte() * 360 / 256f;
        this.yaw = in.readByte() * 360 / 256f;

        int data = in.readInt();
        if(this.type == ObjectType.MINECART) {
            this.data = MagicValues.key(MinecartType.class, data);
        } else if(this.type == ObjectType.ITEM_FRAME) {
            this.data = MagicValues.key(HangingDirection.class, data);
        } else if(this.type == ObjectType.FALLING_BLOCK) {
            this.data = new FallingBlockData(data & 65535, data >> 16);
        } else if(this.type == ObjectType.POTION) {
            this.data = new SplashPotionData(data);
        } else if(this.type == ObjectType.SPECTRAL_ARROW || this.type == ObjectType.FIREBALL || this.type == ObjectType.SMALL_FIREBALL
                || this.type == ObjectType.DRAGON_FIREBALL || this.type == ObjectType.WITHER_SKULL || this.type == ObjectType.FISHING_BOBBER) {
            this.data = new ProjectileData(data);
        } else {
            this.data = new GenericObjectData(data);
        }

        this.motionX = in.readShort() / 8000D;
        this.motionY = in.readShort() / 8000D;
        this.motionZ = in.readShort() / 8000D;
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(this.entityId);
        out.writeUUID(this.uuid);
        out.writeVarInt(MagicValues.value(Integer.class, this.type));
        out.writeDouble(this.x);
        out.writeDouble(this.y);
        out.writeDouble(this.z);
        out.writeByte((byte) (this.pitch * 256 / 360));
        out.writeByte((byte) (this.yaw * 256 / 360));

        int data = 0;
        if(this.data instanceof MinecartType) {
            data = MagicValues.value(Integer.class, this.data);
        } else if(this.data instanceof HangingDirection) {
            data = MagicValues.value(Integer.class, this.data);
        } else if(this.data instanceof FallingBlockData) {
            data = ((FallingBlockData) this.data).getId() | ((FallingBlockData) this.data).getMetadata() << 16;
        } else if(this.data instanceof SplashPotionData) {
            data = ((SplashPotionData) this.data).getPotionData();
        } else if(this.data instanceof ProjectileData) {
            data = ((ProjectileData) this.data).getOwnerId();
        } else if(this.data instanceof GenericObjectData) {
            data = ((GenericObjectData) this.data).getValue();
        }

        out.writeInt(data);

        out.writeShort((int) (this.motionX * 8000));
        out.writeShort((int) (this.motionY * 8000));
        out.writeShort((int) (this.motionZ * 8000));
    }

    @Override
    public boolean isPriority() {
        return false;
    }
}
