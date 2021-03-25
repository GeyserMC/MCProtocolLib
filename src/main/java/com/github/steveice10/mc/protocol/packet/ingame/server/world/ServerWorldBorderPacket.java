package com.github.steveice10.mc.protocol.packet.ingame.server.world;

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.world.WorldBorderAction;
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

@Data
@With
@Setter(AccessLevel.NONE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ServerWorldBorderPacket implements Packet {
    private @NonNull WorldBorderAction action;

    private double newSize;

    private double oldSize;
    private long lerpTime;

    private double newCenterX;
    private double newCenterZ;

    private int newAbsoluteMaxSize;

    private int warningTime;

    private int warningBlocks;

    public ServerWorldBorderPacket(double newSize) {
        this.action = WorldBorderAction.SET_SIZE;

        this.newSize = newSize;
    }

    public ServerWorldBorderPacket(double oldSize, double newSize, long lerpTime) {
        this.action = WorldBorderAction.LERP_SIZE;

        this.oldSize = oldSize;
        this.newSize = newSize;
        this.lerpTime = lerpTime;
    }

    public ServerWorldBorderPacket(double newCenterX, double newCenterZ) {
        this.action = WorldBorderAction.SET_CENTER;

        this.newCenterX = newCenterX;
        this.newCenterZ = newCenterZ;
    }

    public ServerWorldBorderPacket(boolean isTime, int warningValue) {
        if(isTime) {
            this.action = WorldBorderAction.SET_WARNING_TIME;

            this.warningTime = warningValue;
        } else {
            this.action = WorldBorderAction.SET_WARNING_BLOCKS;

            this.warningBlocks = warningValue;
        }
    }

    public ServerWorldBorderPacket(double newCenterX, double newCenterZ, double oldSize, double newSize, long lerpTime, int newAbsoluteMaxSize, int warningTime, int warningBlocks) {
        this.action = WorldBorderAction.INITIALIZE;

        this.newCenterX = newCenterX;
        this.newCenterZ = newCenterZ;
        this.oldSize = oldSize;
        this.newSize = newSize;
        this.lerpTime = lerpTime;
        this.newAbsoluteMaxSize = newAbsoluteMaxSize;
        this.warningTime = warningTime;
        this.warningBlocks = warningBlocks;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.action = MagicValues.key(WorldBorderAction.class, in.readVarInt());
        if(this.action == WorldBorderAction.SET_SIZE) {
            this.newSize = in.readDouble();
        } else if(this.action == WorldBorderAction.LERP_SIZE) {
            this.oldSize = in.readDouble();
            this.newSize = in.readDouble();
            this.lerpTime = in.readVarLong();
        } else if(this.action == WorldBorderAction.SET_CENTER) {
            this.newCenterX = in.readDouble();
            this.newCenterZ = in.readDouble();
        } else if(this.action == WorldBorderAction.INITIALIZE) {
            this.newCenterX = in.readDouble();
            this.newCenterZ = in.readDouble();
            this.oldSize = in.readDouble();
            this.newSize = in.readDouble();
            this.lerpTime = in.readVarLong();
            this.newAbsoluteMaxSize = in.readVarInt();
            this.warningBlocks = in.readVarInt();
            this.warningTime = in.readVarInt();
        } else if(this.action == WorldBorderAction.SET_WARNING_TIME) {
            this.warningTime = in.readVarInt();
        } else if(this.action == WorldBorderAction.SET_WARNING_BLOCKS) {
            this.warningBlocks = in.readVarInt();
        }
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(MagicValues.value(Integer.class, this.action));
        if(this.action == WorldBorderAction.SET_SIZE) {
            out.writeDouble(this.newSize);
        } else if(this.action == WorldBorderAction.LERP_SIZE) {
            out.writeDouble(this.oldSize);
            out.writeDouble(this.newSize);
            out.writeVarLong(this.lerpTime);
        } else if(this.action == WorldBorderAction.SET_CENTER) {
            out.writeDouble(this.newCenterX);
            out.writeDouble(this.newCenterZ);
        } else if(this.action == WorldBorderAction.INITIALIZE) {
            out.writeDouble(this.newCenterX);
            out.writeDouble(this.newCenterZ);
            out.writeDouble(this.oldSize);
            out.writeDouble(this.newSize);
            out.writeVarLong(this.lerpTime);
            out.writeVarInt(this.newAbsoluteMaxSize);
            out.writeVarInt(this.warningBlocks);
            out.writeVarInt(this.warningTime);
        } else if(this.action == WorldBorderAction.SET_WARNING_TIME) {
            out.writeVarInt(this.warningTime);
        } else if(this.action == WorldBorderAction.SET_WARNING_BLOCKS) {
            out.writeVarInt(this.warningBlocks);
        }
    }

    @Override
    public boolean isPriority() {
        return false;
    }
}
