package com.github.steveice10.mc.protocol.packet.ingame.server.entity.player;

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.entity.RotationOrigin;
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
@AllArgsConstructor
public class ServerPlayerFacingPacket implements Packet {
    private @NonNull RotationOrigin origin;
    private double x;
    private double y;
    private double z;

    private int targetEntityId;
    private RotationOrigin targetEntityOrigin;

    public ServerPlayerFacingPacket(RotationOrigin origin, double x, double y, double z) {
        this(origin, x, y, z, 0, null);
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.origin = MagicValues.key(RotationOrigin.class, in.readVarInt());
        this.x = in.readDouble();
        this.y = in.readDouble();
        this.z = in.readDouble();

        if (in.readBoolean()) {
            this.targetEntityId = in.readVarInt();
            this.targetEntityOrigin = MagicValues.key(RotationOrigin.class, in.readVarInt());
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

    @Override
    public boolean isPriority() {
        return false;
    }
}
