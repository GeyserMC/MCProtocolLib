package org.spacehq.mc.protocol.packet.ingame.server.world;

import org.spacehq.mc.protocol.data.game.values.MagicValues;
import org.spacehq.mc.protocol.data.game.values.world.WorldBorderAction;
import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.io.NetOutput;
import org.spacehq.packetlib.packet.Packet;

import java.io.IOException;

public class ServerWorldBorderPacket implements Packet {

    private WorldBorderAction action;

    private double radius;

    private double oldRadius;
    private double newRadius;
    private long speed;

    private double centerX;
    private double centerY;

    private int portalTeleportBoundary;

    private int warningTime;

    private int warningBlocks;

    @SuppressWarnings("unused")
    private ServerWorldBorderPacket() {
    }

    public ServerWorldBorderPacket(double radius) {
        this.action = WorldBorderAction.SET_SIZE;
        this.radius = radius;
    }

    public ServerWorldBorderPacket(double oldRadius, double newRadius, long speed) {
        this.action = WorldBorderAction.LERP_SIZE;
        this.oldRadius = oldRadius;
        this.newRadius = newRadius;
        this.speed = speed;
    }

    public ServerWorldBorderPacket(double centerX, double centerY) {
        this.action = WorldBorderAction.SET_CENTER;
        this.centerX = centerX;
        this.centerY = centerY;
    }

    public ServerWorldBorderPacket(double centerX, double centerY, double oldRadius, double newRadius, long speed, int portalTeleportBoundary, int warningTime, int warningBlocks) {
        this.action = WorldBorderAction.INITIALIZE;
        this.centerX = centerX;
        this.centerY = centerY;
        this.oldRadius = oldRadius;
        this.newRadius = newRadius;
        this.speed = speed;
        this.portalTeleportBoundary = portalTeleportBoundary;
        this.warningTime = warningTime;
        this.warningBlocks = warningBlocks;
    }

    public ServerWorldBorderPacket(int warning, boolean time) {
        if(time) {
            this.action = WorldBorderAction.SET_WARNING_TIME;
            this.warningTime = warning;
        } else {
            this.action = WorldBorderAction.SET_WARNING_BLOCKS;
            this.warningBlocks = warning;
        }
    }

    public WorldBorderAction getAction() {
        return this.action;
    }

    public double getRadius() {
        return this.radius;
    }

    public double getOldRadius() {
        return this.oldRadius;
    }

    public double getNewRadius() {
        return this.newRadius;
    }

    public long getSpeed() {
        return this.speed;
    }

    public double getCenterX() {
        return this.centerX;
    }

    public double getCenterY() {
        return this.centerY;
    }

    public int getPortalTeleportBoundary() {
        return this.portalTeleportBoundary;
    }

    public int getWarningTime() {
        return this.warningTime;
    }

    public int getWarningBlocks() {
        return this.warningBlocks;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.action = MagicValues.key(WorldBorderAction.class, in.readVarInt());
        if(this.action == WorldBorderAction.SET_SIZE) {
            this.radius = in.readDouble();
        } else if(this.action == WorldBorderAction.LERP_SIZE) {
            this.oldRadius = in.readDouble();
            this.newRadius = in.readDouble();
            this.speed = in.readVarLong();
        } else if(this.action == WorldBorderAction.SET_CENTER) {
            this.centerX = in.readDouble();
            this.centerY = in.readDouble();
        } else if(this.action == WorldBorderAction.INITIALIZE) {
            this.centerX = in.readDouble();
            this.centerY = in.readDouble();
            this.oldRadius = in.readDouble();
            this.newRadius = in.readDouble();
            this.speed = in.readVarLong();
            this.portalTeleportBoundary = in.readVarInt();
            this.warningTime = in.readVarInt();
            this.warningBlocks = in.readVarInt();
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
            out.writeDouble(this.radius);
        } else if(this.action == WorldBorderAction.LERP_SIZE) {
            out.writeDouble(this.oldRadius);
            out.writeDouble(this.newRadius);
            out.writeVarLong(this.speed);
        } else if(this.action == WorldBorderAction.SET_CENTER) {
            out.writeDouble(this.centerX);
            out.writeDouble(this.centerY);
        } else if(this.action == WorldBorderAction.INITIALIZE) {
            out.writeDouble(this.centerX);
            out.writeDouble(this.centerY);
            out.writeDouble(this.oldRadius);
            out.writeDouble(this.newRadius);
            out.writeVarLong(this.speed);
            out.writeVarInt(this.portalTeleportBoundary);
            out.writeVarInt(this.warningTime);
            out.writeVarInt(this.warningBlocks);
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
