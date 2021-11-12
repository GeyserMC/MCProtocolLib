package com.github.steveice10.mc.protocol.packet.ingame.clientbound.entity.player;

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.entity.player.PositionElement;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
@With
@AllArgsConstructor
public class ClientboundPlayerPositionPacket implements Packet {
    private final double x;
    private final double y;
    private final double z;
    private final float yaw;
    private final float pitch;
    private final int teleportId;
    private final boolean dismountVehicle;
    private final @NonNull List<PositionElement> relative;

    public ClientboundPlayerPositionPacket(double x, double y, double z, float yaw, float pitch, int teleportId, boolean dismountVehicle, PositionElement... relative) {
        this(x, y, z, yaw, pitch, teleportId, dismountVehicle, Arrays.asList(relative != null ? relative : new PositionElement[0]));
    }

    public ClientboundPlayerPositionPacket(NetInput in) throws IOException {
        this.x = in.readDouble();
        this.y = in.readDouble();
        this.z = in.readDouble();
        this.yaw = in.readFloat();
        this.pitch = in.readFloat();

        this.relative = new ArrayList<>();
        int flags = in.readUnsignedByte();
        for (PositionElement element : PositionElement.values()) {
            int bit = 1 << MagicValues.value(Integer.class, element);
            if ((flags & bit) == bit) {
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
        for (PositionElement element : this.relative) {
            flags |= 1 << MagicValues.value(Integer.class, element);
        }

        out.writeByte(flags);

        out.writeVarInt(this.teleportId);
        out.writeBoolean(this.dismountVehicle);
    }
}
