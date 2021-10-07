package com.github.steveice10.mc.protocol.packet.ingame.client.world;

import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.With;

import java.io.IOException;

@Data
@With
@Setter(AccessLevel.NONE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ClientSteerVehiclePacket implements Packet {
    private static final int FLAG_JUMP = 0x01;
    private static final int FLAG_DISMOUNT = 0x02;

    private float sideways;
    private float forward;
    private boolean jump;
    private boolean dismount;

    @Override
    public void read(NetInput in) throws IOException {
        this.sideways = in.readFloat();
        this.forward = in.readFloat();

        int flags = in.readUnsignedByte();
        this.jump = (flags & FLAG_JUMP) != 0;
        this.dismount = (flags & FLAG_DISMOUNT) != 0;
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeFloat(this.sideways);
        out.writeFloat(this.forward);

        int flags = 0;
        if (this.jump) {
            flags |= FLAG_JUMP;
        }

        if (this.dismount) {
            flags |= FLAG_DISMOUNT;
        }

        out.writeByte(flags);
    }

    @Override
    public boolean isPriority() {
        return false;
    }
}
