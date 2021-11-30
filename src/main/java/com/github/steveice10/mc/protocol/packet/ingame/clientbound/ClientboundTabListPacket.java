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
public class ClientboundTabListPacket implements Packet {
    private final @NonNull Component header;
    private final @NonNull Component footer;

    public ClientboundTabListPacket(NetInput in) throws IOException {
        this.header = DefaultComponentSerializer.get().deserialize(in.readString());
        this.footer = DefaultComponentSerializer.get().deserialize(in.readString());
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeString(DefaultComponentSerializer.get().serialize(this.header));
        out.writeString(DefaultComponentSerializer.get().serialize(this.footer));
    }
}
