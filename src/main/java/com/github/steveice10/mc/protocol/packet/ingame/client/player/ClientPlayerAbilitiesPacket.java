package com.github.steveice10.mc.protocol.packet.ingame.client.player;

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
public class ClientPlayerAbilitiesPacket implements Packet {
    private static final int FLAG_FLYING = 0x02;

    private boolean flying;

    @Override
    public void read(NetInput in) throws IOException {
        byte flags = in.readByte();
        this.flying = (flags & FLAG_FLYING) > 0;
    }

    @Override
    public void write(NetOutput out) throws IOException {
        int flags = 0;

        if (this.flying) {
            flags |= FLAG_FLYING;
        }

        out.writeByte(flags);
    }

    @Override
    public boolean isPriority() {
        return false;
    }
}
