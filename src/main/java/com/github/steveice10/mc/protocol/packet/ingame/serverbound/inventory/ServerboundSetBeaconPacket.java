package com.github.steveice10.mc.protocol.packet.ingame.serverbound.inventory;

import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;

import java.io.IOException;
import java.util.OptionalInt;

@Data
@With
@AllArgsConstructor
public class ServerboundSetBeaconPacket implements Packet {
    private final OptionalInt primaryEffect;
    private final OptionalInt secondaryEffect;

    public ServerboundSetBeaconPacket(NetInput in) throws IOException {
        if (in.readBoolean()) {
            this.primaryEffect = OptionalInt.of(in.readVarInt());
        } else {
            this.primaryEffect = OptionalInt.empty();
        }

        if (in.readBoolean()) {
            this.secondaryEffect = OptionalInt.of(in.readVarInt());
        } else {
            this.secondaryEffect = OptionalInt.empty();
        }
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeBoolean(this.primaryEffect.isPresent());
        if (this.primaryEffect.isPresent()) {
            out.writeVarInt(this.primaryEffect.getAsInt());
        }

        out.writeBoolean(this.secondaryEffect.isPresent());
        if (this.secondaryEffect.isPresent()) {
            out.writeVarInt(this.secondaryEffect.getAsInt());
        }
    }
}
