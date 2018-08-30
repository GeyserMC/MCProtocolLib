package com.github.steveice10.mc.protocol.packet.ingame.server.entity.player;

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.entity.FeetOrEyes;
import com.github.steveice10.mc.protocol.packet.MinecraftPacket;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;

import java.io.IOException;

public class ServerPlayerFacingPacket extends MinecraftPacket {
    private FeetOrEyes origin; // presumably the origin from which pitch is calculated at
    private double x;
    private double y;
    private double z;
    private Integer targetEntityId;
    private FeetOrEyes targetEntityFeetOrEyes;

    @SuppressWarnings("unused")
    private ServerPlayerFacingPacket() {
    }

    public ServerPlayerFacingPacket(FeetOrEyes origin, double x, double y, double z) {
        this.origin = origin;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public ServerPlayerFacingPacket(FeetOrEyes origin, int targetEntityId, FeetOrEyes lookAt) {
        this.origin = origin;
        this.targetEntityId = targetEntityId;
        this.targetEntityFeetOrEyes = lookAt;
    }

    public FeetOrEyes getOrigin() {
        return origin;
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

    public Integer getTargetEntityId() {
        return targetEntityId;
    }

    public FeetOrEyes getTargetEntityFeetOrEyes() {
        return targetEntityFeetOrEyes;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.origin = MagicValues.key(FeetOrEyes.class, in.readVarInt());
        this.x = in.readDouble();
        this.y = in.readDouble();
        this.z = in.readDouble();
        if (in.readBoolean()) {
            this.targetEntityId = in.readVarInt();
            this.targetEntityFeetOrEyes = MagicValues.key(FeetOrEyes.class, in.readVarInt());
        }
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(MagicValues.value(Integer.class, this.origin));
        out.writeDouble(this.x);
        out.writeDouble(this.y);
        out.writeDouble(this.z);
        if (this.targetEntityId != null) {
            out.writeBoolean(true);
            out.writeVarInt(this.targetEntityId);
            out.writeVarInt(MagicValues.value(Integer.class, this.targetEntityFeetOrEyes));
        } else {
            out.writeBoolean(false);
        }
    }
}
