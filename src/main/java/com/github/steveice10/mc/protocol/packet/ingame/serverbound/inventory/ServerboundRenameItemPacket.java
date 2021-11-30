package com.github.steveice10.mc.protocol.packet.ingame.serverbound.inventory;

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
public class ServerboundRenameItemPacket implements Packet {
    private final @NonNull String name;

    public ServerboundRenameItemPacket(NetInput in) throws IOException {
        this.name = in.readString();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeString(this.name);
    }
}
