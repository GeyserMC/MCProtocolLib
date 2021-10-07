package com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn;

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.entity.object.FallingBlockData;
import com.github.steveice10.mc.protocol.data.game.entity.object.GenericObjectData;
import com.github.steveice10.mc.protocol.data.game.entity.object.HangingDirection;
import com.github.steveice10.mc.protocol.data.game.entity.object.MinecartType;
import com.github.steveice10.mc.protocol.data.game.entity.object.ObjectData;
import com.github.steveice10.mc.protocol.data.game.entity.object.ProjectileData;
import com.github.steveice10.mc.protocol.data.game.entity.object.SplashPotionData;
import com.github.steveice10.mc.protocol.data.game.entity.type.EntityType;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.With;

import java.io.IOException;
import java.util.UUID;

@Data
@With
@Setter(AccessLevel.NONE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ServerSpawnEntityPacket implements Packet {
    private static final GenericObjectData EMPTY_DATA = new GenericObjectData(0);

    private int entityId;
    private @NonNull UUID uuid;
    private @NonNull EntityType type;
    private @NonNull ObjectData data;
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;
    private double motionX;
    private double motionY;
    private double motionZ;

    public ServerSpawnEntityPacket(int entityId, @NonNull UUID uuid, @NonNull EntityType type,
                                   double x, double y, double z, float yaw, float pitch) {
        this(entityId, uuid, type, EMPTY_DATA, x, y, z, yaw, pitch, 0, 0, 0);
    }

    public ServerSpawnEntityPacket(int entityId, @NonNull UUID uuid, @NonNull EntityType type, @NonNull ObjectData data,
                                   double x, double y, double z, float yaw, float pitch) {
        this(entityId, uuid, type, data, x, y, z, yaw, pitch, 0, 0, 0);
    }

    public ServerSpawnEntityPacket(int entityId, @NonNull UUID uuid, @NonNull EntityType type,
                                   double x, double y, double z, float yaw, float pitch,
                                   double motionX, double motionY, double motionZ) {
        this(entityId, uuid, type, EMPTY_DATA, x, y, z, yaw, pitch, motionX, motionY, motionZ);
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.entityId = in.readVarInt();
        this.uuid = in.readUUID();
        this.type = MagicValues.key(EntityType.class, in.readVarInt());
        this.x = in.readDouble();
        this.y = in.readDouble();
        this.z = in.readDouble();
        this.pitch = in.readByte() * 360 / 256f;
        this.yaw = in.readByte() * 360 / 256f;

        int data = in.readInt();
        if (this.type == EntityType.MINECART) {
            this.data = MagicValues.key(MinecartType.class, data);
        } else if (this.type == EntityType.ITEM_FRAME || this.type == EntityType.GLOW_ITEM_FRAME) {
            this.data = MagicValues.key(HangingDirection.class, data);
        } else if (this.type == EntityType.FALLING_BLOCK) {
            this.data = new FallingBlockData(data & 65535, data >> 16);
        } else if (this.type == EntityType.THROWN_POTION) {
            this.data = new SplashPotionData(data);
        } else if (this.type == EntityType.SPECTRAL_ARROW || this.type == EntityType.FIREBALL || this.type == EntityType.SMALL_FIREBALL
                || this.type == EntityType.DRAGON_FIREBALL || this.type == EntityType.WITHER_SKULL || this.type == EntityType.FISHING_BOBBER) {
            this.data = new ProjectileData(data);
        } else {
            if (data == 0) {
                this.data = EMPTY_DATA;
            } else {
                this.data = new GenericObjectData(data);
            }
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
        if (this.data instanceof MinecartType) {
            data = MagicValues.value(Integer.class, this.data);
        } else if (this.data instanceof HangingDirection) {
            data = MagicValues.value(Integer.class, this.data);
        } else if (this.data instanceof FallingBlockData) {
            data = ((FallingBlockData) this.data).getId() | ((FallingBlockData) this.data).getMetadata() << 16;
        } else if (this.data instanceof SplashPotionData) {
            data = ((SplashPotionData) this.data).getPotionData();
        } else if (this.data instanceof ProjectileData) {
            data = ((ProjectileData) this.data).getOwnerId();
        } else if (this.data instanceof GenericObjectData) {
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
