package com.github.steveice10.mc.protocol.packet.ingame.serverbound.window;

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
@AllArgsConstructor
public class ServerboundSetBeaconPacket implements Packet {
    private final int primaryEffect;
    private final int secondaryEffect;

    public ServerboundSetBeaconPacket(NetInput in) throws IOException {
        this.primaryEffect = in.readVarInt();
        this.secondaryEffect = in.readVarInt();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(this.primaryEffect);
        out.writeVarInt(this.secondaryEffect);
    }
}
