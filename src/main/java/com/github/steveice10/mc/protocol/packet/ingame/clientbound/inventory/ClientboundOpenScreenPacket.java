package com.github.steveice10.mc.protocol.packet.ingame.clientbound.inventory;

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.inventory.ContainerType;
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
public class ClientboundOpenScreenPacket implements Packet {
    private final int containerId;
    private final @NonNull ContainerType type;
    private final @NonNull String name;

    public ClientboundOpenScreenPacket(NetInput in) throws IOException {
        this.containerId = in.readVarInt();
        this.type = MagicValues.key(ContainerType.class, in.readVarInt());
        this.name = in.readString();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(this.containerId);
        out.writeVarInt(MagicValues.value(Integer.class, this.type));
        out.writeString(this.name);
    }
}
