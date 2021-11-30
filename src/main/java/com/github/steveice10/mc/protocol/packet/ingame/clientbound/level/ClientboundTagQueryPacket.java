package com.github.steveice10.mc.protocol.packet.ingame.clientbound.level;

import com.github.steveice10.mc.protocol.data.game.NBT;
import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
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
public class ClientboundTagQueryPacket implements Packet {
    private final int transactionId;
    private final @NonNull CompoundTag nbt;

    public ClientboundTagQueryPacket(NetInput in) throws IOException {
        this.transactionId = in.readVarInt();
        this.nbt = NBT.read(in);
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(this.transactionId);
        NBT.write(out, this.nbt);
    }
}
