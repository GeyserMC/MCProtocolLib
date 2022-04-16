package com.github.steveice10.mc.protocol.packet.ingame.serverbound.inventory;

import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;

import javax.annotation.Nullable;
import java.io.IOException;

@Data
@With
@AllArgsConstructor
public class ServerboundSetBeaconPacket implements Packet {
    private final Integer primaryEffect;
    private final Integer secondaryEffect;

    public ServerboundSetBeaconPacket(NetInput in) throws IOException {
        if (in.readBoolean()) {
            this.primaryEffect = in.readVarInt();
        } else {
            this.primaryEffect = null;
        }
        if (in.readBoolean()) {
            this.secondaryEffect = in.readVarInt();
        } else {
            this.secondaryEffect = null;
        }
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeBoolean(this.primaryEffect > 0);
        out.writeVarInt(this.primaryEffect);
        out.writeBoolean(this.secondaryEffect > 0);
        out.writeVarInt(this.secondaryEffect);
    }
}
