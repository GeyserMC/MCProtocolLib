package com.github.steveice10.mc.protocol.packet.ingame.serverbound.player;

import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;

import java.io.IOException;

@Data
@With
@AllArgsConstructor
public class ServerboundPlayerAbilitiesPacket implements Packet {
    private static final int FLAG_FLYING = 0x02;

    private final boolean flying;

    public ServerboundPlayerAbilitiesPacket(NetInput in) throws IOException {
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
}
