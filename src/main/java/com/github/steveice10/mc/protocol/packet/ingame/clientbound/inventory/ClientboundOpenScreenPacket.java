package com.github.steveice10.mc.protocol.packet.ingame.clientbound.inventory;

import com.github.steveice10.mc.protocol.data.DefaultComponentSerializer;
import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.inventory.ContainerType;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import net.kyori.adventure.text.Component;

import java.io.IOException;

@Data
@With
@AllArgsConstructor
public class ClientboundOpenScreenPacket implements Packet {
    private final int containerId;
    private final @NonNull ContainerType type;
    private final @NonNull Component title;

    public ClientboundOpenScreenPacket(NetInput in) throws IOException {
        this.containerId = in.readVarInt();
        this.type = MagicValues.key(ContainerType.class, in.readVarInt());
        this.title = DefaultComponentSerializer.get().deserialize(in.readString());
    }

    @Deprecated
    public ClientboundOpenScreenPacket(int containerId, @NonNull ContainerType type, @NonNull String name) {
        this.containerId = containerId;
        this.type = type;
        this.title = DefaultComponentSerializer.get().deserialize(name);
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(this.containerId);
        out.writeVarInt(MagicValues.value(Integer.class, this.type));
        out.writeString(DefaultComponentSerializer.get().serialize(this.title));
    }

    @Deprecated
    public String getName() {
        return DefaultComponentSerializer.get().serialize(title);
    }

    @Deprecated
    public ClientboundOpenScreenPacket withName(String name) {
        return new ClientboundOpenScreenPacket(this.containerId, this.type, DefaultComponentSerializer.get().deserialize(name));
    }
}
