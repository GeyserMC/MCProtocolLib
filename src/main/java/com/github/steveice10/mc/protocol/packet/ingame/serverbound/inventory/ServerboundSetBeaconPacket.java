package com.github.steveice10.mc.protocol.packet.ingame.serverbound.inventory;

import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.OptionalInt;

@Data
@With
@AllArgsConstructor
public class ServerboundSetBeaconPacket implements Packet {
    private final @NonNull OptionalInt primaryEffect;
    private final @NonNull OptionalInt secondaryEffect;

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
        if (this.primaryEffect.isPresent()) {
            out.writeBoolean(true);
            out.writeVarInt(this.primaryEffect.getAsInt());
        } else {
            out.writeBoolean(false);
        }

        if (this.secondaryEffect.isPresent()) {
            out.writeBoolean(true);
            out.writeVarInt(this.secondaryEffect.getAsInt());
        } else {
            out.writeBoolean(false);
        }
    }
}
