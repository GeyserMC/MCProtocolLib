package com.github.steveice10.mc.protocol.packet.ingame.clientbound;

import com.github.steveice10.mc.protocol.data.DefaultComponentSerializer;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.With;
import net.kyori.adventure.text.Component;

import java.io.IOException;

@Data
@With
@Setter(AccessLevel.NONE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ClientboundTabListPacket implements Packet {
    private @NonNull Component header;
    private @NonNull Component footer;

    @Override
    public void read(NetInput in) throws IOException {
        this.header = DefaultComponentSerializer.get().deserialize(in.readString());
        this.footer = DefaultComponentSerializer.get().deserialize(in.readString());
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeString(DefaultComponentSerializer.get().serialize(this.header));
        out.writeString(DefaultComponentSerializer.get().serialize(this.footer));
    }

    @Override
    public boolean isPriority() {
        return false;
    }
}
