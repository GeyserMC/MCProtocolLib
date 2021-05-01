package com.github.steveice10.mc.protocol.packet.ingame.server.entity.player;

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.entity.player.PositionElement;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
@With
@Setter(AccessLevel.NONE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ServerPlayerPositionRotationPacket implements Packet {
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;
    private int teleportId;
    private boolean dismountVehicle;
    private @NonNull List<PositionElement> relative;

    public ServerPlayerPositionRotationPacket(double x, double y, double z, float yaw, float pitch, int teleportId, boolean dismountVehicle, PositionElement... relative) {
        this(x, y, z, yaw, pitch, teleportId, dismountVehicle, Arrays.asList(relative != null ? relative : new PositionElement[0]));
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.x = in.readDouble();
        this.y = in.readDouble();
        this.z = in.readDouble();
        this.yaw = in.readFloat();
        this.pitch = in.readFloat();

        this.relative = new ArrayList<>();
        int flags = in.readUnsignedByte();
        for(PositionElement element : PositionElement.values()) {
            int bit = 1 << MagicValues.value(Integer.class, element);
            if((flags & bit) == bit) {
                this.relative.add(element);
            }
        }

        this.teleportId = in.readVarInt();
        this.dismountVehicle = in.readBoolean();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeDouble(this.x);
        out.writeDouble(this.y);
        out.writeDouble(this.z);
        out.writeFloat(this.yaw);
        out.writeFloat(this.pitch);

        int flags = 0;
        for(PositionElement element : this.relative) {
            flags |= 1 << MagicValues.value(Integer.class, element);
        }

        out.writeByte(flags);

        out.writeVarInt(this.teleportId);
        out.writeBoolean(this.dismountVehicle);
    }

    @Override
    public boolean isPriority() {
        return false;
    }
}
