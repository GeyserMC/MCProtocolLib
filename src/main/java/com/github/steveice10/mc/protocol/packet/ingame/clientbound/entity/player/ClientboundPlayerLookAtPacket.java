package com.github.steveice10.mc.protocol.packet.ingame.clientbound.entity.player;

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.entity.RotationOrigin;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;

import java.io.IOException;

@Data
@With
@AllArgsConstructor
public class ClientboundPlayerLookAtPacket implements Packet {
    private final @NonNull RotationOrigin origin;
    private final double x;
    private final double y;
    private final double z;

    private final int targetEntityId;
    private final RotationOrigin targetEntityOrigin;

    public ClientboundPlayerLookAtPacket(RotationOrigin origin, double x, double y, double z) {
        this(origin, x, y, z, 0, null);
    }

    public ClientboundPlayerLookAtPacket(NetInput in) throws IOException {
        this.origin = MagicValues.key(RotationOrigin.class, in.readVarInt());
        this.x = in.readDouble();
        this.y = in.readDouble();
        this.z = in.readDouble();

        if (in.readBoolean()) {
            this.targetEntityId = in.readVarInt();
            this.targetEntityOrigin = MagicValues.key(RotationOrigin.class, in.readVarInt());
        } else {
            this.targetEntityId = 0;
            this.targetEntityOrigin = null;
        }
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(MagicValues.value(Integer.class, this.origin));
        out.writeDouble(this.x);
        out.writeDouble(this.y);
        out.writeDouble(this.z);

        if (this.targetEntityOrigin != null) {
            out.writeBoolean(true);
            out.writeVarInt(this.targetEntityId);
            out.writeVarInt(MagicValues.value(Integer.class, this.targetEntityOrigin));
        } else {
            out.writeBoolean(false);
        }
    }
}
