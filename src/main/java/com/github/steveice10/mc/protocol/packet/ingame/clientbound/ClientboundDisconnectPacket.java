package com.github.steveice10.mc.protocol.packet.ingame.clientbound;

import com.github.steveice10.mc.protocol.data.DefaultComponentSerializer;
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
public class ClientboundDisconnectPacket implements Packet {
    private final @NonNull Component reason;

    public ClientboundDisconnectPacket(@NonNull String reason) {
        this(DefaultComponentSerializer.get().deserialize(reason));
    }

    public ClientboundDisconnectPacket(NetInput in) throws IOException {
        this.reason = DefaultComponentSerializer.get().deserialize(in.readString());
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeString(DefaultComponentSerializer.get().serialize(this.reason));
    }

    @Override
    public boolean isPriority() {
        return true;
    }
}
