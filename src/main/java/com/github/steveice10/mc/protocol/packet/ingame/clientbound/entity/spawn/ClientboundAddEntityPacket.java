package com.github.steveice10.mc.protocol.packet.ingame.clientbound.entity.spawn;

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.entity.object.Direction;
import com.github.steveice10.mc.protocol.data.game.entity.object.FallingBlockData;
import com.github.steveice10.mc.protocol.data.game.entity.object.GenericObjectData;
import com.github.steveice10.mc.protocol.data.game.entity.object.MinecartType;
import com.github.steveice10.mc.protocol.data.game.entity.object.ObjectData;
import com.github.steveice10.mc.protocol.data.game.entity.object.ProjectileData;
import com.github.steveice10.mc.protocol.data.game.entity.object.SplashPotionData;
import com.github.steveice10.mc.protocol.data.game.entity.type.EntityType;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;

import java.io.IOException;
import java.util.UUID;

@Data
@With
@AllArgsConstructor
public class ClientboundAddEntityPacket implements Packet {
    private static final GenericObjectData EMPTY_DATA = new GenericObjectData(0);

    private final int entityId;
    private final @NonNull UUID uuid;
    private final @NonNull EntityType type;
    private final @NonNull ObjectData data;
    private final double x;
    private final double y;
    private final double z;
    private final float yaw;
    private final float pitch;
    private final double motionX;
    private final double motionY;
    private final double motionZ;

    public ClientboundAddEntityPacket(int entityId, @NonNull UUID uuid, @NonNull EntityType type,
                                      double x, double y, double z, float yaw, float pitch) {
        this(entityId, uuid, type, EMPTY_DATA, x, y, z, yaw, pitch, 0, 0, 0);
    }

    public ClientboundAddEntityPacket(int entityId, @NonNull UUID uuid, @NonNull EntityType type, @NonNull ObjectData data,
                                      double x, double y, double z, float yaw, float pitch) {
        this(entityId, uuid, type, data, x, y, z, yaw, pitch, 0, 0, 0);
    }

    public ClientboundAddEntityPacket(int entityId, @NonNull UUID uuid, @NonNull EntityType type,
                                      double x, double y, double z, float yaw, float pitch,
                                      double motionX, double motionY, double motionZ) {
        this(entityId, uuid, type, EMPTY_DATA, x, y, z, yaw, pitch, motionX, motionY, motionZ);
    }

    public ClientboundAddEntityPacket(NetInput in) throws IOException {
        this.entityId = in.readVarInt();
        this.uuid = in.readUUID();
        this.type = EntityType.read(in);
        this.x = in.readDouble();
        this.y = in.readDouble();
        this.z = in.readDouble();
        this.pitch = in.readByte() * 360 / 256f;
        this.yaw = in.readByte() * 360 / 256f;

        int data = in.readInt();
        if (this.type == EntityType.MINECART) {
            this.data = MagicValues.key(MinecartType.class, data);
        } else if (this.type == EntityType.ITEM_FRAME || this.type == EntityType.GLOW_ITEM_FRAME) {
            this.data = Direction.VALUES[data];
        } else if (this.type == EntityType.FALLING_BLOCK) {
            this.data = new FallingBlockData(data & 65535, data >> 16);
        } else if (this.type == EntityType.POTION) {
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
        out.writeVarInt(this.type.ordinal());
        out.writeDouble(this.x);
        out.writeDouble(this.y);
        out.writeDouble(this.z);
        out.writeByte((byte) (this.pitch * 256 / 360));
        out.writeByte((byte) (this.yaw * 256 / 360));

        int data = 0;
        if (this.data instanceof MinecartType) {
            data = MagicValues.value(Integer.class, this.data);
        } else if (this.data instanceof Direction) {
            data = ((Direction) this.data).ordinal();
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
}
